from djongo import models
from djongo.models import forms
from django.utils import timezone
from django.db.models.base import ModelBase
from django.forms import widgets

class Blog(models.Model):
    name = models.CharField(max_length=100)
    tagline = models.TextField()

    class Meta:
        abstract = True
class BlogForm(forms.ModelForm):

    class Meta:
        model = Blog
        fields = (
            'name', 'tagline'
        )

class Rescue(models.Model):
    name = models.CharField(max_length=100)
    commander = models.CharField(max_length=100)
    lon = models.FloatField()
    lat = models.FloatField()
    timestart = models.DateTimeField(default=timezone.now)
    timestop = models.DateTimeField(default=timezone.now,null=True)
    def __str__(self):
        return self.name
    # objects = models.DjongoManager()
class RescueForm(forms.ModelForm):
    class Meta:
        model = Rescue
        fields = (
            'name','timestart','timestop','commander','lon','lat'
        )
class Devicedata(models.Model):
    timestamp = models.DateTimeField(default=timezone.now,blank=True, null=True)
    healthtype = models.IntegerField(default=1,blank=True, null=True)
    respirations = models.FloatField(blank=True, null=True)
    perfusion = models.FloatField(blank=True, null=True)
    lon = models.FloatField(blank=True, null=True)
    lat = models.FloatField(blank=True, null=True)



class DevicedataForm(forms.ModelForm):
    class Meta:
        model = Devicedata
        fields = (
            'timestamp','healthtype','respirations','perfusion','lon','lat'
        )
#
class route(models.Model):
    lon = models.FloatField(blank=True, null=True)
    lat = models.FloatField(blank=True, null=True)
class routeForm(forms.ModelForm):
    class Meta:
        model = route
        fields = [
            'lon','lat'
        ]
class routes(models.Model):
    device = models.CharField(max_length=200)
    datas = models.ArrayModelField(
        model_container=route,
        model_form_class=routeForm,
        blank=True,null=True,

    )
    def __str__(self):
        return self.device

class Devices(models.Model):
    DeviceId = models.CharField(max_length=200)
    rescue = models.ForeignKey(Rescue, on_delete=models.CASCADE,blank=True, null=True)
    name = models.CharField(blank=True,max_length=100, null=True)
    SEX_CHOICES = (
        ('F', 'Female',),
        ('M', 'Male',),
        ('U', 'Unsure',),
    )
    sex = models.CharField(
        blank=True,
        max_length=1,
        choices=SEX_CHOICES,
    )
    birthdate = models.DateField(blank=True, null=True)
    height = models.FloatField(blank=True,null=True)
    weight = models.FloatField(blank=True,null=True)
    age = models.IntegerField(blank=True,null=True)
    homeplace = models.CharField(max_length=100,blank=True,null=True)
    livingplace = models.CharField(max_length=100,blank=True,null=True)
    nationality = models.CharField(max_length=100,blank=True,null=True)
    hospital = models.CharField(max_length=100,blank=True,null=True)
    datas = models.ArrayModelField(
        model_container=Devicedata,
        model_form_class=DevicedataForm,
        blank=True,null=True
    )
    inhospital = models.BooleanField(blank=True,null = False)
    # objects = models.DjongoManager()
    def __str__(self):
        return self.DeviceId
    # objects = models.DjongoManager()
class DevicesForm(forms.ModelForm):
    birthdate = forms.DateField(widget=forms.TextInput(
        attrs={
            'class':'vDateField'
        }
    ),required=False)
    class Meta:
        model = Devices
        fields = [
            'DeviceId','rescue','name','sex','birthdate','height','weight','age','homeplace','livingplace','nationality','hospital','inhospital'
        ]



class Entry(models.Model):
    blog = models.EmbeddedModelField(
        model_container=Blog,
        model_form_class=BlogForm
    )

    headline = models.CharField(max_length=255)
