# coding: utf-8
from __future__ import print_function
import tensorflow as tf
from nets import nets_factory
from preprocessing import preprocessing_factory
import utils
import os

slim = tf.contrib.slim


def gram(layer):
    shape = tf.shape(layer) #获取张量
    num_images = shape[0] #张量第一层 表示图像数量
    width = shape[1] #张量第二层 长
    height = shape[2] #张量第三层 宽
    num_filters = shape[3] #过滤器的数量
    filters = tf.reshape(layer, tf.stack([num_images, -1, num_filters]))
    grams = tf.matmul(filters, filters, transpose_a=True) / tf.to_float(width * height * num_filters)

    return grams


def get_style_features(FLAGS):
    """
    For the "style_image", the preprocessing step is:
    1. Resize the shorter side to FLAGS.image_size
    2. Apply central crop
    对于“ style_image”，预处理步骤为：
    1.将短边调整为FLAGS.image_size
    2.应用
    """
    with tf.Graph().as_default():
        network_fn = nets_factory.get_network_fn(
            FLAGS.loss_model,
            num_classes=1,
            is_training=False)
        image_preprocessing_fn, image_unprocessing_fn = preprocessing_factory.get_preprocessing(
            FLAGS.loss_model,
            is_training=False)

        # Get the style image data 获取风格图片数据
        size = FLAGS.image_size
        img_bytes = tf.read_file(FLAGS.style_image)
        if FLAGS.style_image.lower().endswith('png'):
            image = tf.image.decode_png(img_bytes)
        else:
            image = tf.image.decode_jpeg(img_bytes)

        # Add the batch dimension 添加批尺寸 Batch_Size（批尺寸）是机器学习中一个重要参数
        images = tf.expand_dims(image_preprocessing_fn(image, size, size), 0)

        _, endpoints_dict = network_fn(images, spatial_squeeze=False)
        features = []
        for layer in FLAGS.style_layers:
            feature = endpoints_dict[layer]
            feature = tf.squeeze(gram(feature), [0])  # remove the batch dimension
            features.append(feature)

        with tf.Session() as sess:
            # Restore variables for loss network. 恢复损失网络的变量
            init_func = utils._get_init_fn(FLAGS)
            init_func(sess)

            # Make sure the 'generated' directory is exists.
            if os.path.exists('generated') is False:
                os.makedirs('generated')
            # Indicate cropped style image path 确保“生成的”目录存在。
            save_file = 'generated/target_style_' + FLAGS.naming + '.jpg'
            # Write preprocessed style image to indicated path 将预处理的样式图像写入指示的路径
            with open(save_file, 'wb') as f:
                target_image = image_unprocessing_fn(images[0, :])
                value = tf.image.encode_jpeg(tf.cast(target_image, tf.uint8))
                f.write(sess.run(value))
                tf.logging.info('Target style pattern is saved to: %s.' % save_file)

            # Return the features those layers are use for measuring style loss. 返回这些图层用于测量样式损失的要素。
            return sess.run(features)

# 定义风格损失
# style_layers为定义使用哪些层计算风格损失。默认为conv1_2、conv2_2、conv3_3、conv4_3
# style_features_t是利用原始的风格图片计算的层的激活
# 如在wave模型中是img/wave.jpg计算的激活
def style_loss(endpoints_dict, style_features_t, style_layers):
    style_loss = 0
    # summary是为TensorBoard服务的
    style_loss_summary = {}
    for style_gram, layer in zip(style_features_t, style_layers):
        # 计算风格损失，只需要计算生成图片generated_imgs与目标风格
        # style_features_t的差距。因此不需要取出content_images
        generated_images, _ = tf.split(endpoints_dict[layer], 2, 0)
        size = tf.size(generated_images)
        # 调用gram函数计算Gram矩阵。风格损失定义为生成图片与目标风格Gram矩阵的L*L的Loss
        layer_style_loss = tf.nn.l2_loss(gram(generated_images) - style_gram) * 2 / tf.to_float(size)
        style_loss_summary[layer] = layer_style_loss
        style_loss += layer_style_loss
    return style_loss, style_loss_summary

# endpoints_dict是上一节提到的损失网络各层的计算结果
# content_layers是定义使用哪些层的差距计算损失，默认配置是conv3_3
def content_loss(endpoints_dict, content_layers):
    content_loss = 0
    for layer in content_layers:
        # 上一节中把生成的图像、原始图像同时传入损失网络中计算
        # 所以这里先把他们区分开
        # 读者可以参照函数tf.concat与tf.split的文档理解此处的内容
        generated_images, content_images = tf.split(endpoints_dict[layer], 2, 0)
        size = tf.size(generated_images)
        # 所谓的内容损失，是生成图片generated_images与原始图片激活content_images的L*L距离
        content_loss += tf.nn.l2_loss(generated_images - content_images) * 2 / tf.to_float(size)  # remain the same as in the paper
    return content_loss


def total_variation_loss(layer):
    shape = tf.shape(layer)
    height = shape[1]
    width = shape[2]
    y = tf.slice(layer, [0, 0, 0, 0], tf.stack([-1, height - 1, -1, -1])) - tf.slice(layer, [0, 1, 0, 0], [-1, -1, -1, -1])
    x = tf.slice(layer, [0, 0, 0, 0], tf.stack([-1, -1, width - 1, -1])) - tf.slice(layer, [0, 0, 1, 0], [-1, -1, -1, -1])
    loss = tf.nn.l2_loss(x) / tf.to_float(tf.size(x)) + tf.nn.l2_loss(y) / tf.to_float(tf.size(y))
    return loss
