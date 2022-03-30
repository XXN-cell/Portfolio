from django.http import HttpResponse
from django.shortcuts import render
import uuid,os
from webapp.models import Image
import eval
import tensorflow as tf
import time
# Create your views here.
def hello(request):
    return render(request,'hello.html')

def uplogic(request):
    try:
        print('111')
        file = request.FILES.get('source')
        print(file.name)
        file.name = generateUUID(file.name)
        sele =request.POST.get('sele')
        image = Image.objects.create(pic=file)
        eval.FLAGS.model_file = 'models/'+sele+'.ckpt-done'
        eval.FLAGS.image_file = 'static/pics/'+file.name
        tf.logging.set_verbosity(tf.logging.INFO)
        tf.app.run(eval.test)
    finally:
        return render(request,'output.html',{'image':'generated/'+file.name})



def generateUUID(filename):
    id = str(uuid.uuid4())
    extend = os.path.splitext(filename)[1]
    return id+extend

def query(request):
    images = Image.objects.all()
    print(images[0].pic.url)
    return render(request,'output.html',{'images':images})