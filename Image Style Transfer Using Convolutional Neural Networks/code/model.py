import tensorflow as tf
import numpy as np
# conv2d卷积函数
def conv2d(x, input_filters, output_filters, kernel, strides, mode='REFLECT'): # x 输入通道数 输出通道数 核 步长 mode：通过对称轴进行对称复制的方式进行填充
    with tf.variable_scope('conv'):

        shape = [kernel, kernel, input_filters, output_filters] #定义一个维度
        weight = tf.Variable(tf.truncated_normal(shape, stddev=0.1), name='weight') #定义一个weight变量 truncated_normal:shape 维度 stddev标准差 这个函数产生的随机数与均值的差距不会超过两倍的标准差[ - 2 * stddev,  2 * stddev] ;卷积核:长 宽 输入通道数 输出通道数
        x_padded = tf.pad(x, [[0, 0], [np.int(kernel / 2), np.int(kernel / 2)], [np.int(kernel / 2), np.int(kernel / 2)], [0, 0]], mode=mode) #扩展x padding为边框 mode为方法对称轴进行对称复制的方式进行填充
        return tf.nn.conv2d(x_padded, weight, strides=[1, strides, strides, 1], padding='VALID', name='conv') #需要做卷积的输入图像x_padded weight卷积核 strides：[1，长上步长，宽上步长，1] padding valid模式在边缘采取的是“不及”的方法

#conv2d 反卷积函数
def conv2d_transpose(x, input_filters, output_filters, kernel, strides):
    with tf.variable_scope('conv_transpose'):

        shape = [kernel, kernel, output_filters, input_filters] #定义一个维度
        weight = tf.Variable(tf.truncated_normal(shape, stddev=0.1), name='weight') #定义一个weight变量 truncated_normal:shape 维度 stddev标准差 这个函数产生的随机数与均值的差距不会超过两倍的标准差[ - 2 * stddev,  2 * stddev] ;卷积核:长 宽 输入通道数 输出通道数

        batch_size = tf.shape(x)[0] #取x张量的第一维作为batch_size
        height = tf.shape(x)[1] * strides #长
        width = tf.shape(x)[2] * strides #宽
        output_shape = tf.stack([batch_size, height, width, output_filters]) #返回一个被提高维度的张量 矩阵拼接 将batch_size, height, width, output_filters拼接 成为一个高维矩阵
        return tf.nn.conv2d_transpose(x, weight, output_shape, strides=[1, strides, strides, 1], name='conv_transpose') #


def resize_conv2d(x, input_filters, output_filters, kernel, strides, training):
    '''
    反卷积的另一个替代方法 先调整大小然后进行卷积
    An alternative to transposed convolution where we first resize, then convolve.
    See http://distill.pub/2016/deconv-checkerboard/

    '''
    with tf.variable_scope('conv_transpose'):
        height = x.get_shape()[1].value if training else tf.shape(x)[1]
        width = x.get_shape()[2].value if training else tf.shape(x)[2]

        new_height = height * strides * 2
        new_width = width * strides * 2

        x_resized = tf.image.resize_images(x, [new_height, new_width], tf.image.ResizeMethod.NEAREST_NEIGHBOR) #最近邻插值法，将变换后的图像中的原像素点最邻近像素的灰度值赋给原像素点的方法，返回图像张量dtype与所传入的相同。

        # shape = [kernel, kernel, input_filters, output_filters]
        # weight = tf.Variable(tf.truncated_normal(shape, stddev=0.1), name='weight')
        return conv2d(x_resized, input_filters, output_filters, kernel, strides)


def instance_norm(x):
    epsilon = 1e-9

    mean, var = tf.nn.moments(x, [1, 2], keep_dims=True)

    return tf.div(tf.subtract(x, mean), tf.sqrt(tf.add(var, epsilon)))


def batch_norm(x, size, training, decay=0.999):
    beta = tf.Variable(tf.zeros([size]), name='beta')
    scale = tf.Variable(tf.ones([size]), name='scale')
    pop_mean = tf.Variable(tf.zeros([size]))
    pop_var = tf.Variable(tf.ones([size]))
    epsilon = 1e-3

    batch_mean, batch_var = tf.nn.moments(x, [0, 1, 2])
    train_mean = tf.assign(pop_mean, pop_mean * decay + batch_mean * (1 - decay))
    train_var = tf.assign(pop_var, pop_var * decay + batch_var * (1 - decay))

    def batch_statistics():
        with tf.control_dependencies([train_mean, train_var]):
            return tf.nn.batch_normalization(x, batch_mean, batch_var, beta, scale, epsilon, name='batch_norm')

    def population_statistics():
        return tf.nn.batch_normalization(x, pop_mean, pop_var, beta, scale, epsilon, name='batch_norm')

    return tf.cond(training, batch_statistics, population_statistics)

# 非数化零 线性整流函数 神经网络中的激励函数
def relu(input):
    relu = tf.nn.relu(input) #tf.nn.relu()函数的目的是，将输入小于0的值幅值为0，输入大于0的值不变。
    # convert nan to zero (nan != nan)
    nan_to_zero = tf.where(tf.equal(relu, relu), relu, tf.zeros_like(relu)) #tf.equal(A, B)是对比这两个矩阵或者向量的相等的元素，如果是相等的那就返回True，反正返回False，返回的值的矩阵维度和A是一样的 zeros_like：新建一个与给定的tensor类型大小一致的tensor，其所有元素为0 where：返回输入矩阵中true的位置
    return nan_to_zero

# 类残差网络函数
def residual(x, filters, kernel, strides):
    with tf.variable_scope('residual'):
        conv1 = conv2d(x, filters, filters, kernel, strides)
        conv2 = conv2d(relu(conv1), filters, filters, kernel, strides)

        residual = x + conv2

        return residual


def net(image, training):
    #给图片周围加上边框，目的是消除边缘效应
    image = tf.pad(image, [[0, 0], [10, 10], [10, 10], [0, 0]], mode='REFLECT')

    #三层卷积层
    with tf.variable_scope('conv1'): #用于定义创建变量（层）的操作的
        conv1 = relu(instance_norm(conv2d(image, 3, 32, 9, 1)))
    with tf.variable_scope('conv2'):
        conv2 = relu(instance_norm(conv2d(conv1, 32, 64, 3, 2)))
    with tf.variable_scope('conv3'):
        conv3 = relu(instance_norm(conv2d(conv2, 64, 128, 3, 2)))

    #模仿ResNet定义
    with tf.variable_scope('res1'):
        res1 = residual(conv3, 128, 3, 1)
    with tf.variable_scope('res2'):
        res2 = residual(res1, 128, 3, 1)
    with tf.variable_scope('res3'):
        res3 = residual(res2, 128, 3, 1)
    with tf.variable_scope('res4'):
        res4 = residual(res3, 128, 3, 1)
    with tf.variable_scope('res5'):
        res5 = residual(res4, 128, 3, 1)
    # print(res5.get_shape())
    #定义卷积之后定义反卷积
    #反卷积不采用通常的转置卷积的方式，而是采用先放大，在做卷积的方式这样可以消除噪声点
    with tf.variable_scope('deconv1'):
        # deconv1 = relu(instance_norm(conv2d_transpose(res5, 128, 64, 3, 2)))
        deconv1 = relu(instance_norm(resize_conv2d(res5, 128, 64, 3, 2, training)))
    with tf.variable_scope('deconv2'):
        # deconv2 = relu(instance_norm(conv2d_transpose(deconv1, 64, 32, 3, 2)))
        deconv2 = relu(instance_norm(resize_conv2d(deconv1, 64, 32, 3, 2, training)))
    with tf.variable_scope('deconv3'):
        # deconv_test = relu(instance_norm(conv2d(deconv2, 32, 32, 2, 1)))
        deconv3 = tf.nn.tanh(instance_norm(conv2d(deconv2, 32, 3, 9, 1)))
    # decanv3是经过tanh函数得到的输出值，所以他的阈值范围是-1～1
    #对deconv3进行缩放 RGB范围0-255
    y = (deconv3 + 1) * 127.5

    # 移除边框
    height = tf.shape(y)[1]
    width = tf.shape(y)[2]
    y = tf.slice(y, [0, 10, 10, 0], tf.stack([-1, height - 20, width - 20, -1]))

    return y
