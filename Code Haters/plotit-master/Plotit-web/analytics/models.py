from django.db import models

# Create your models here.

class Data(models.Model):
    file_name = models.FileField(upload_to="Files/",null=False)
