from django.db import models

# Create your models here.
class Image(models.Model):
    inputImage = models.ImageField(upload_to='pics')
    pic = models.FileField(upload_to='pics')