"""mci_rest_djongo URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/2.0/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path, include
from django.conf.urls import url
from django.conf import settings
from django.conf.urls.static import static
from app import views as appview
urlpatterns = [
    path('admin/', admin.site.urls),
    path('index/', appview.rescuelist),
    path('rescuelist/', appview.rescuelist),
    path('gotohospital/', appview.gotohospital),
    path('queryRescue/', appview.queryRescue),
    path('queryDevice/', appview.queryDevice),
    path('createRescue/', appview.androidcreaterescue),
    path('personaldetailedit/', appview.mobilepersonaledit),
    path('addhealthdata/', appview.addhealthdata),
    path('queryRescuepk/', appview.queryRescuepk),
    url('queryDevicepk/(?P<pk>\d+)/$', appview.querydevice_pk),
    url('personaldetail/(?P<pk>\d+)/$', appview.mobilequerydevice),
    path('easyRescue/', appview.easycreateRescue),
    path('deleteRescue/',appview.deleteRescue),
    url(r'^easyEditRescue/(.+)/$',appview.easyEditRescue),
    url(r'^rescue/$', appview.createRescue),
    url(r'^devicelist/(.+)/',appview.devicelist),
    url(r'^device/(?P<pk>\d+)/$',appview.devicedetail, name='device'),
    url(r'^editdevice/(?P<pk>\d+)/$',appview.editdevice, name='editdevice'),
    url(r'^createDevice/$',appview.createDevice, name='createDevice'),
    url(r'^medical/(?P<pk>\d+)/$',appview.medical, name='medical'),
    url(r'^mapsdata/(?P<pk>\d+)/$',appview.mapsdata, name='mapsdata'),
    url(r'^trajors/(?P<rescuename>[-\w]+)/$',appview.trajors, name='trajors'),
    url(r'^statuschart/(?P<rescuename>[-\w]+)/$',appview.statuschart, name='statuschart'),
    url(r'^excel/(?P<rescuename>[-\w]+)/$',appview.excel, name='excel'),
]
if settings.DEBUG:
    urlpatterns += static(settings.STATIC_URL, document_root=settings.STATIC_ROOT)
