from django.contrib import admin

# Register your models here.
from .models import Entry,Rescue,Devices,route,routes

admin.site.register(Entry)
admin.site.register(Devices)
admin.site.register(Rescue)
admin.site.register(route)
admin.site.register(routes)
# admin.site.register(DevicePersonal)
# admin.site.register(Devicedata)
