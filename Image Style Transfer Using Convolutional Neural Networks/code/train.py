# coding: utf-8
from __future__ import print_function
from __future__ import division
import tensorflow as tf
from nets import nets_factory
from preprocessing import preprocessing_factory
import reader
import model
import time
import losses
import utils
import os
import argparse

slim = tf.contrib.slim


def parse_args():
    parser = argparse.ArgumentParser()
    parser.add_argument('-c', '--conf', default='conf/mosaic.yml', help='the path to the conf file')
    return parser.parse_args()


def main(FLAGS):
    style_features_t = losses.get_style_features(FLAGS)

    # Make sure the training path exists.
    training_path = os.path.join(FLAGS.model_path, FLAGS.naming)
    if not(os.path.exists(training_path)):
        os.makedirs(training_path)

    with tf.Graph().as_default():
        with tf.Session() as sess:
            """Build Network"""
            # network_fn是损失网络的函数。因为不需要对损失函数训练，所以is_training = False
            network_fn = nets_factory.get_network_fn(
                FLAGS.loss_model,
                num_classes=1,
                is_training=False)

            # 损失网络中要用的图像的预处理函数
            image_preprocessing_fn, image_unprocessing_fn = preprocessing_factory.get_preprocessing(
                FLAGS.loss_model,
                is_training=False)

            # 读入训练图像
            processed_images = reader.image(FLAGS.batch_size, FLAGS.image_size, FLAGS.image_size,
                                            'train2014/', image_preprocessing_fn, epochs=FLAGS.epoch)

            # 此处引用图像生成网络。model.net是图像生成网络,generated是生成的图像
            # 设置training = True，因为要训练该网络
            generated = model.net(processed_images, training=True)

            # 将生成的图像generated同样使用image_preprocessing_fn进行处理
            # 因为generated同样需要送到损失网络中计算loss
            processed_generated = [image_preprocessing_fn(image, FLAGS.image_size, FLAGS.image_size)
                                   for image in tf.unstack(generated, axis=0, num=FLAGS.batch_size)
                                   ]
            processed_generated = tf.stack(processed_generated)

            # 将原始图像、生成图像送到损失网络中
            # 这里将它们合并后再送到网络中计算，因为同一的计算可以加快速度
            # 将原始图像、生成图像送到损失网络并计算后，将使用结果endpoints_dict 计算损失
            _, endpoints_dict = network_fn(tf.concat([processed_generated, processed_images], 0), spatial_squeeze=False)

            # Log the structure of loss network
            tf.logging.info('Loss network layers(You can define them in "content_layers" and "style_layers"):')
            for key in endpoints_dict:
                tf.logging.info(key)

            """Build Losses"""
            # 定义内容损失
            content_loss = losses.content_loss(endpoints_dict, FLAGS.content_layers)
            # 定义风格损失
            style_loss, style_loss_summary = losses.style_loss(endpoints_dict, style_features_t, FLAGS.style_layers)
            # 定义tv损失，该损失在实际训练中并没有被应道，因为在训练时都采用tv_weight=0
            tv_loss = losses.total_variation_loss(generated)  # use the unprocessed image
            # 总损失是这些损失的加权和，最后利用总损失优化图像生成网络即可
            loss = FLAGS.style_weight * style_loss + FLAGS.content_weight * content_loss + FLAGS.tv_weight * tv_loss

            """Prepare to Train 准备训练"""
            global_step = tf.Variable(0, name="global_step", trainable=False) #设定trainable=False 可以防止该变量被数据流图的 GraphKeys.TRAINABLE_VARIABLES 收集, 这样我们就不会在训练的时候尝试更新它的值。

            variable_to_train = [] #需要训练的列表
            for variable in tf.trainable_variables(): # 迭代出可训练的变量进行训练
                if not(variable.name.startswith(FLAGS.loss_model)): #如果这些变量名字里不是以loss_model的数据开头的就加入训练列表
                    variable_to_train.append(variable)
            """
            loss：Tensor包含最小化值的A。
            global_step：可选Variable，在变量更新后增加一。
            var_list：可选的Variable对象列表或元组，以进行更新以最小化loss。默认为键下图形中收集的变量列"""
            train_op = tf.train.AdamOptimizer(1e-3).minimize(loss, global_step=global_step, var_list=variable_to_train) #函数是Adam优化算法：自适应矩估计优化算法 是一个寻找全局最优点的优化算法，引入了二次方梯度校正。

            variables_to_restore = []
            for v in tf.global_variables():
                if not(v.name.startswith(FLAGS.loss_model)):
                    variables_to_restore.append(v)
            saver = tf.train.Saver(variables_to_restore, write_version=tf.train.SaverDef.V1)

            sess.run([tf.global_variables_initializer(), tf.local_variables_initializer()])

            # Restore variables for loss network. 恢复损失网络的变量。
            init_func = utils._get_init_fn(FLAGS)
            init_func(sess)

            # Restore variables for training model if the checkpoint file exists. 如果检查点文件存在，则恢复训练模型的变量
            last_file = tf.train.latest_checkpoint(training_path)
            if last_file:
                tf.logging.info('Restoring model from {}'.format(last_file))
                saver.restore(sess, last_file)

            """Start Training 开始训练"""
            coord = tf.train.Coordinator()
            threads = tf.train.start_queue_runners(coord=coord)
            start_time = time.time()
            try:
                while not coord.should_stop():
                    _, loss_t, step = sess.run([train_op, loss, global_step])
                    elapsed_time = time.time() - start_time
                    start_time = time.time()
                    """logging"""
                    # print(step)
                    if step % 10 == 0:
                        tf.logging.info('step: %d,  total Loss %f, secs/step: %f' % (step, loss_t, elapsed_time))
                    """checkpoint"""
                    if step % 1000 == 0:
                        saver.save(sess, os.path.join(training_path, 'fast-style-model.ckpt'), global_step=step)
            except tf.errors.OutOfRangeError:
                saver.save(sess, os.path.join(training_path, 'fast-style-model.ckpt-done'))
                tf.logging.info('Done training -- epoch limit reached')
            finally:
                coord.request_stop()
            coord.join(threads)


if __name__ == '__main__':
    tf.logging.set_verbosity(tf.logging.INFO)
    args = parse_args()
    FLAGS = utils.read_conf_file(args.conf)
    main(FLAGS)
