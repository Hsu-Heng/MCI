from django.shortcuts import render, get_object_or_404,render_to_response,render,redirect
from .models import Devices,Rescue,RescueForm,DevicesForm,Devicedata,DevicedataForm,route,routes
from django.http import HttpResponseRedirect,HttpResponse, JsonResponse
import json
import datetime
def rescuelist(request):
    return render(request,'./rescue/rescuelist.html',locals())
def devicelist(request,param1):
    d = {"obj": {"rescuename":param1}}
    print(d)
    return render_to_response('./device/devicelist.html',d)
def generatedata(request):
    if request.method=='GET':
        deviceid = '手環ID_05'
        files = open("/home/hsu/Downloads/05.txt",'r')
        lines = files.readlines()
        latlngs = []
        for r in lines:
            try:
                s = r.strip()
                s = s.split(",")
                print(s[1],s[0])
                latlng = route(lon = float(s[1]),lat =  float(s[0]))
                latlng.save()
                latlngs.append(latlng)
            except:
                print('error')

        r1 = routes.objects.create(device = deviceid,datas = latlngs)
        r1.save()
    return HttpResponse("ok")

def urlprocess(param1,param2):
    receive_rescue = str(param1)
    receive_device = str(param2)
    if "/" in param1:
        slicecount = param1.find("/")
        receive_rescue = param1[:slicecount]
        receive_device = param1[slicecount+1:]
    return receive_rescue,receive_device

def devicedetail(request,pk):
    deviceset = Devices.objects.get(pk = pk)
    d = {"obj": {"DeviceId":deviceset.DeviceId,"rescue":deviceset.rescue.name,"name":deviceset.name,"sex":deviceset.sex,
             "birthdate": deviceset.birthdate, "height": deviceset.height,"weight":deviceset.weight,"pk": deviceset.pk,
             "age":deviceset.age,"homeplace":deviceset.homeplace,"livingplace":deviceset.livingplace,"nationality":deviceset.nationality,
             "hospital":deviceset.hospital,"inhospital":deviceset.inhospital,"res":"/devicelist/"+deviceset.rescue.name+"/"}}
    print(d)

    return render_to_response('./device/device_detail.html',d)
def gotohospital(request):
    if request.method=='POST':
        json_data = json.loads(request.body.decode("utf-8"))
        try:
            for r in json_data:

                device = Devices.objects.get(pk = json_data[r])
                device.inhospital = True
                print(r)
                device.save()
        except:
            print('error')
    return HttpResponse(json.dumps({'ok':'ok'}),content_type="application/json")
def addhealthdata(request):
    myjson = {}
    pks = 0
    if request.method=='POST':
        json_data = json.loads(request.body.decode("utf-8"))
        print(json_data)
        if "create" in json_data:
            try:

                device = Devices.objects.get(DeviceId = json_data['device_id'])
                pks = device.pk
                print(pks,device.pk)


            except:
                rescue = Rescue.objects.get(name = json_data['rescue'])
                print("qwerror")
                deviceid = json_data['device_id']
                datas = Devicedata(respirations = float(json_data['respirations']),healthtype = int(json_data['healthtype']),
                        perfusion = float(json_data['perfusion']),lon = float(json_data['lon']),lat = float(json_data['lat']))
                datas.save()
                dataslist = []
                dataslist.append(datas)
                r1 = Devices.objects.create(DeviceId = str(deviceid),rescue= rescue,datas = dataslist,inhospital=False)
                r1.save()
                pks = r1.pk

        else:
            try:
                device = Devices.objects.get(DeviceId = json_data['device_id'])
                odatas = Devicedata(respirations = float(json_data['respirations']),healthtype = int(json_data['healthtype']),
                        perfusion = float(json_data['perfusion']),lon = float(json_data['lon']),lat = float(json_data['lat']))
                device.datas.append(odatas)
                device.inhospital == False
                device.save()
                pks = device.pk
            except:
                print("qwerror")


        # data = Devicedata(healthtype = json_data['healthtype'], respirations = json_data['respirations'],
        #         perfusion = json_data['perfusion'],lon = json_data['lon'], lat = json_data['lat'] )
        # device.datas.append(data)
        # device.save()
        myjson['pk'] = pks
    return HttpResponse(json.dumps(myjson),content_type="application/json")
def excel(request,rescuename):
    rescue = Rescue.objects.get(name=rescuename)
    deviceset = Devices.objects.all().filter(rescue = rescue)
    jsonarray = []
    for r in deviceset:
        djson = {}
        djson['姓名'] = r.name
        djson['性別'] = r.sex
        djson['年齡'] = r.age
        djson['身高'] = r.height
        djson['體重'] = r.weight
        s = ['','黑色','紅色','黃色','綠色']
        if r.inhospital == True:
            djson['送診'] = '有'
        else:
            djson['送診'] = '無'


        st = s[r.datas[-1].healthtype]
        djson['檢傷'] = st
        jsonarray.append(djson)


    return HttpResponse(json.dumps(jsonarray),content_type="application/json")
def trajors(request,rescuename):
    rescue = Rescue.objects.get(name=rescuename)
    deviceset = Devices.objects.all().filter(rescue = rescue)
    traj = {}
    nowtraj = {}
    for r in deviceset:
        r1 = routes.objects.get(device = r.DeviceId)
        pailong = {}
        pailong['color'] = r.datas[-1].healthtype
        pailong['lon'] = r.datas[-1].lon
        pailong['lat'] = r.datas[-1].lat
        pailong['hospital'] = r.inhospital
        nowtraj[r.DeviceId] = pailong
        postitions = []
        for postition in r1.datas:
            xy = [postition.lon,postition.lat]
            postitions.append(xy)
        traj[r.DeviceId] = postitions
    result = {}
    result['traj'] = traj
    result['now'] = nowtraj
    print(nowtraj)
    return HttpResponse(json.dumps(result),content_type="application/json")
def statuschart(request,rescuename):
    rescue = Rescue.objects.get(name=rescuename)
    deviceset = Devices.objects.all().filter(rescue = rescue)
    stasus = {}
    send = {}
    myjson = {}
    s = ['','黑色-死亡','紅色-極危險','黃色-危險','綠色-輕傷']
    stasus[s[1]] = 0
    stasus[s[2]] = 0
    stasus[s[3]] = 0
    stasus[s[4]] = 0
    send['待送診'] = 0
    send['送診'] = 0
    for r in deviceset:
        st = s[r.datas[-1].healthtype]
        if st in stasus.keys():
            stasus[st] = stasus[st]+1
        else:
            stasus[st] = 1
        print(r.DeviceId,st)
        sd = r.inhospital

        if sd == True:
            if '送診' in send.keys():
                send['送診'] = send['送診']+1
            else:
                send['送診'] = 1
        elif sd == False:
            if '待送診' in send.keys():
                send['待送診'] = send['待送診']+1
            else:
                send['待送診'] = 1

    myjson['stasus'] = stasus
    myjson['inhospital'] = send
    print('myjson'+str(myjson))
    return HttpResponse(json.dumps(myjson),content_type="application/json")
def mapsdata(request,pk):
    deviceset = Devices.objects.get(pk = pk)
    datalist = deviceset.datas
    response = {}
    response['timestamp'] = []
    response['lon'] = []
    response['lat'] = []
    for i in range(0,len(datalist)-1):
        response['timestamp'].append(datalist[i].timestamp)
        response['lon'].append(datalist[i].lon)
        response['lat'].append(datalist[i].lat)
    response['length'] = len(datalist)-1
    print(response)
    return JsonResponse(response)
def medical(request,pk):
    deviceset = Devices.objects.get(pk = pk)
    datalist = deviceset.datas
    lendata = len(datalist)
    response = {}
    response['timestamp'] = []
    response['healthtype'] = []
    response['respirations'] = []
    response['perfusion'] = []
    response['lon'] = []
    response['lat'] = []
    datastart = 0
    if lendata >= 5:
        for i in range(0,lendata):
            response['timestamp'].append(i+1)
            response['healthtype'].append(datalist[i].healthtype)
            response['respirations'].append(datalist[i].respirations)
            response['perfusion'].append(datalist[i].perfusion)
            response['lon'].append(datalist[i].lon)
            response['lat'].append(datalist[i].lat)
        response['length'] = lendata
    else:
        for i in range(0,lendata-1):
            response['timestamp'].append(i+1)
            response['healthtype'].append(datalist[i].healthtype)
            response['respirations'].append(datalist[i].respirations)
            response['perfusion'].append(datalist[i].perfusion)
            response['lon'].append(datalist[i].lon)
            response['lat'].append(datalist[i].lat)

        response['length'] = lendata-1


    return JsonResponse(response)
# def editdevice(request,pk):
def createDevice(request):
    if request.method=="GET":
        form = DevicesForm()
        form1 = DevicedataForm()
        return render(request, './device/createdevice.html', {'form': form,'form1':form1})
    elif request.method=="POST":
        # form = DevicesForm(request.POST)
        rescue = Rescue.objects.get(pk=request.POST['rescue'])
        deviceid = request.POST['DeviceId']
        datas = Devicedata(respirations = request.POST['respirations'],timestamp = request.POST['timestamp'],healthtype = request.POST['healthtype'],
                perfusion = request.POST['perfusion'],lon = request.POST['lon'],lat = request.POST['lat'])
        datas.save()
        dataslist = []
        dataslist.append(datas)
        r1 = Devices.objects.create(DeviceId = str(deviceid),rescue= rescue,datas = dataslist)
        r1.save()
        return HttpResponseRedirect('/devicelist/'+rescue.name+'/')





def editdevice(request,pk):
    device = get_object_or_404(Devices, pk=pk)
    if request.method=="POST":
        form = DevicesForm(request.POST, instance=device)
        if form.is_valid():
            device = form.save(commit=False)

            device.save()
        return redirect('device', pk=pk)
    else:
        deviceset = Devices.objects.get(pk = pk)
        form = DevicesForm(instance=deviceset)
        return render(request, './device/editDevice.html', {'form': form})

def queryRescuepk(request):
    if request.method == 'GET':
        newdata = []
        myjson = {}
        rescueset = Rescue.objects.all()
        for r in rescueset:
            x = {}
            data  = Rescue.objects.get(name = r)
            x['name'] = data.name
            x['pk'] = data.pk
            newdata.append(x)
        newdata.reverse()
        print(newdata)
        myjson['total'] = len(rescueset)
        myjson['rows'] = newdata
    return HttpResponse(json.dumps(myjson),content_type="application/json")
# Create your views here.
def queryRescue(request):
    newdata = []
    myjson = {}
    rescueset = Rescue.objects.all()
    print("here")
    if request.method == 'POST':
        received_json_data = json.loads(request.body.decode("utf-8"))
        print(received_json_data)
        limit = received_json_data['limit']
        offset = received_json_data['offset']
        total = len(rescueset)
        end = offset+limit
        if end > total:
            end=total
        for i in range(offset,end):
            x = {}
            x['name'] = rescueset[i].name
            x['commander'] = rescueset[i].commander
            x['lat'] = rescueset[i].lat
            x['lon'] = rescueset[i].lon
            x['address'] = getaddress(str(rescueset[i].lon),str(rescueset[i].lat))
            x['timestart'] = rescueset[i].timestart.strftime("%Y-%m-%d %H:%M:%S")
            x['timeend'] = rescueset[i].timestop.strftime("%Y-%m-%d %H:%M:%S")

            deviceset = Devices.objects.all().filter(rescue = rescueset[i])
            x['black'] = 0
            x['red'] = 0
            x['yellow'] = 0
            x['green'] = 0
            x['inhospital'] = 0
            x['notinhospital'] = 0
            for r in deviceset:
                datalist = r.datas
                lendata = len(datalist)
                color = datalist[lendata-1].healthtype
                if color == 1:
                    x['black'] = x['black']+1
                elif color == 2:
                    x['red'] = x['red']+1
                elif color == 3:
                    x['yellow'] = x['yellow']+1
                elif color == 4:
                    x['green'] = x['green']+1
                hospital = r.inhospital
                if hospital == True:
                    x['inhospital'] = x['inhospital'] +1
                else:
                    x['notinhospital'] = x['notinhospital'] +1

            x['patients'] = len(deviceset)

            newdata.append(x)
        myjson['total'] = total
        myjson['rows'] = newdata
        print(newdata)
    else:
        for r in rescueset:
            x = {}
            data  = Rescue.objects.get(name = r)
            x['name'] = data.name
            x['commander'] = data.commander
            x['lat'] = data.lat
            x['lon'] = data.lon
            # x['address'] = getaddress(str(rescueset[i].lon),str(rescueset[i].lat))
            x['timestart'] = data.timestart.strftime("%Y-%m-%d %H:%M:%S")
            x['timeend'] = data.timestop.strftime("%Y-%m-%d %H:%M:%S")
            x['pk'] = data.pk
            newdata.append(x)
        newdata.reverse()
        print(newdata)
        myjson['total'] = len(rescueset)
        myjson['rows'] = newdata

    return HttpResponse(json.dumps(myjson),content_type="application/json")
def getaddress(lat,lon):
    import urllib.request
    try:
        url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="+lon+","+lat+"&sensor=false&language=zh-tw"
        print(url)
        address = urllib.request.urlopen(url)
        address = address.read().decode('utf-8')
        xjson =  json.loads(address)
        result = xjson['results'][0]['formatted_address']
        print(result)
        return result
    except:
        return ""

def querydevice_pk(request, pk):
    newdata = []
    myjson = {}
    rescue = Rescue.objects.get(pk=pk)
    deviceset = Devices.objects.all().filter(rescue = rescue)
    stasus = {}
    send = {}

    s = ['','黑色','紅色','黃色','綠色']
    for r in deviceset:
        datalist = r.datas
        lendata = len(datalist)
        ds = datalist[lendata-1].healthtype

        hea = s[ds]
        if hea in stasus.keys():
            stasus[hea] = stasus[hea]+1
        else:
            stasus[hea] = 1
        sd = r.inhospital
        print(r.DeviceId,sd)
        if sd == True:
            if '已配送' in send.keys():
                send['已配送'] = send['已配送']+1
            else:
                send['已配送'] = 1
        elif sd == False:
            if '未配送' in send.keys():
                send['未配送'] = send['未配送']+1
            else:
                send['未配送'] = 1
        x = {}
        x['name'] = r.name
        x['DeviceId'] = r.DeviceId
        x['pk'] = r.pk
        x['status'] = ds
        x['sex'] = str(r.sex)
        x['age'] = str(r.age)
        x['weight'] = str(r.weight)
        x['height'] = str(r.height)
        x['inhospital'] = r.inhospital
        # x['height'] = r.height
        newdata.append(x)
    myjson['stasus'] = stasus
    myjson['inhospital'] = send
    myjson['count'] = len(deviceset)
    myjson['data'] = newdata
    print(myjson)
    return HttpResponse(json.dumps(myjson),content_type="application/json")
def queryDevice(request):
    newdata = []
    myjson = {}
    colors = ['','死亡','極危險','危險','輕傷']
    print(request)

    if request.method == 'POST':
        received_json_data = json.loads(request.body.decode("utf-8"))

        print(received_json_data)
        receive_rescue = received_json_data['rescue']
        limit = received_json_data['limit']
        offset = received_json_data['offset']
        rescue = Rescue.objects.get(name=receive_rescue)
        print(rescue)
        deviceset = Devices.objects.all().filter(rescue = rescue)
        print(Devices.objects.all().filter(rescue = rescue))
        total = len(deviceset)
        end = offset+limit
        if end > total:
            end=total

        for i in range(offset,end):
            x = {}
            x['DeviceId'] = deviceset[i].DeviceId
            x['healthtype'] = deviceset[i].datas[-1].healthtype
            x['type'] = colors[deviceset[i].datas[-1].healthtype]
            x['heart'] = deviceset[i].datas[-1].perfusion
            x['so2'] = deviceset[i].datas[-1].respirations
            if deviceset[i].inhospital==True:
                x['hospital'] = '送診'
            else:
                x['hospital'] = '未送診'
            x['pk'] = deviceset[i].pk
            newdata.append(x)
        newdata = sorted(newdata, key = lambda i: i['healthtype'],reverse=False)
        myjson['total'] = total
        myjson['rows'] = newdata

    else:
        deviceset = Devices.objects.all()
        for r in deviceset:
            x = {}
            data  = Devices.objects.get(DeviceId = r)
            x['DeviceId'] = deviceset[i].DeviceId
            x['healthtype'] = deviceset[i].datas[-1].healthtype
            x['pk'] = deviceset[i].pk
            newdata.append(x)
        sorted(newdata, key = lambda i: i['healthtype'],reverse=True)
        myjson['total'] = len(rescueset)
        myjson['rows'] = newdata
        result = json.dumps(myjson)
    return HttpResponse(json.dumps(myjson),content_type="application/json")

def createRescue(request):
    if request.method == 'POST':
        form = RescueForm(request.POST)
        if form.is_valid():
            # post = form.save()
            print(form)
            # x = Rescue.objects.get(pk=post.pk)
            # print(x.name)
            return render(request, './rescue/rescue.html', {'form': form})

    else:
        form = RescueForm()
    return render(request, './rescue/rescue.html', {'form': form})
def androidcreaterescue(request):
    if request.method=='POST':

        received_json_data = json.loads(request.body.decode("utf-8"))
        print(received_json_data)
        name = received_json_data['name']
        commander = received_json_data['commander']
        lon = received_json_data['lon']
        lat = received_json_data['lat']
        datestart = received_json_data['timestart']
        datetimestart = datetime.datetime.strptime(datestart, "%Y-%m-%d %H:%M:%S")

        r1 = Rescue(name = name, commander=commander, lon = lon, lat = lat, timestart = datetimestart)
        r1.save()
        return HttpResponse("ok")
def mobilepersonaledit(request):
    myjson = {}
    if request.method == 'POST':
        received_json_data = json.loads(request.body.decode("utf-8"))
        print(received_json_data)
        device = Devices.objects.get(pk=received_json_data['pk'])
        rescues = Rescue.objects.get( name=received_json_data['rescue'])
        device.sex = received_json_data['sex']
        device.birthdate = datetime.datetime.strptime(received_json_data['birthdate'], "%Y/%m/%d")
        device.age = received_json_data['age']
        device.height = received_json_data['height']
        device.weight = received_json_data['weight']
        device.hospital = received_json_data['hospital']
        device.name = received_json_data['name']
        device.save()
        myjson['sss'] = "ok"
    return HttpResponse(json.dumps(myjson),content_type="application/json")
def mobilequerydevice( request, pk):
    myjson = {}
    if request.method == 'GET':
        rescuelist = Rescue.objects.all()
        rescuearray = []
        for r in rescuelist:
            rescuearray.append(r.name)
        devicerequest = Devices.objects.get(pk = pk)
        myjson['DeviceId'] = devicerequest.DeviceId
        myjson['rescue'] = devicerequest.rescue.name
        myjson['name'] = devicerequest.name
        myjson['sex'] = devicerequest.sex
        try:
            myjson['birthdate'] = devicerequest.birthdate.strftime("%Y/%m/%d")
        except:
             myjson['birthdate'] = devicerequest.birthdate
        myjson['height'] = devicerequest.height
        myjson['weight'] = devicerequest.weight
        myjson['age'] = devicerequest.age
        myjson['inhospital'] = devicerequest.inhospital
        myjson['hospital'] = devicerequest.hospital
        myjson['rescuearray'] = rescuearray
        print(myjson)
    return HttpResponse(json.dumps(myjson),content_type="application/json")

def easycreateRescue( request):
    if request.method == 'GET':
        now = datetime.datetime.now()
        datestart = now.strftime("%Y-%m-%d")
        timestart = now.strftime("%H:%M:%S")
        d = {"obj": {"startdate": datestart, "starttime": timestart}}
        return render_to_response('./rescue/easy_createrescue.html',d)
    elif request.method == 'POST':
        form = RescueForm(request.POST)
        if form.is_valid():
            post = form.save()
            print('ok')
        print(request.POST)
        print(request.POST['name'])
        name = request.POST['name']
        commander = request.POST['commander']
        lon = request.POST['lon']
        lat = request.POST['lat']
        datestart = request.POST['timestart_0'] + ' ' + request.POST['timestart_1']
        datetimestart = datetime.datetime.strptime(datestart, "%Y-%m-%d %H:%M:%S")
        datestop = request.POST['timestop_0'] + ' ' + request.POST['timestop_1']
        datetimestop = datetime.datetime.strptime(datestop, "%Y-%m-%d %H:%M:%S")
        r1 = Rescue(name = name, commander=commander, lon = lon, lat = lat, timestart = datetimestart, timestop = datetimestop)
        r1.save()
        return render(request,'./rescue/rescuelist.html',locals())
def easyEditRescue(request,params):
    if request.method == 'GET':
        record = Rescue.objects.get(name = params)
        name = record.name
        commander = record.commander
        lon = record.lon
        lat = record.lat
        datestart = record.timestart.strftime("%Y-%m-%d")
        timestart = record.timestart.strftime("%H:%M:%S")
        datestop = record.timestop.strftime("%Y-%m-%d")
        timestop = record.timestop.strftime("%H:%M:%S")
        d = {"obj": {"name":name,"commander":commander,"lon":lon,"lat":lat,"startdate": datestart, "starttime": timestart,"stopdate":datestop,
             "stoptime":timestop}}
        return render_to_response('./rescue/easy_edit.html',d)
    elif request.method == 'POST':
        form = RescueForm(request.POST)
        if form.is_valid():
            post = form.save()
            print('ok')
        print(request.POST)
        print(request.POST['name'])
        name = request.POST['name']
        commander = request.POST['commander']
        lon = request.POST['lon']
        lat = request.POST['lat']
        datestart = request.POST['timestart_0'] + ' ' + request.POST['timestart_1']
        datetimestart = datetime.datetime.strptime(datestart, "%Y-%m-%d %H:%M:%S")
        datestop = request.POST['timestop_0'] + ' ' + request.POST['timestop_1']
        datetimestop = datetime.datetime.strptime(datestop, "%Y-%m-%d %H:%M:%S")
        record = Rescue.objects.get(name = params)
        record.name = name
        record.commander = commander
        record.lon = lon
        record.lat = lat
        record.timestart = datetimestart
        record.timestop = datetimestop
        record.save()
        return render(request,'./rescue/rescuelist.html',locals())
def deleteRescue(request):
    if request.method == 'POST':
        # data = request.body.decode('utf-8')
        received_json_data = json.loads(request.POST['mydata'])
        # print(received_json_data)
        Datalistname = []
        for r in received_json_data:
            Datalistname.append(r['name'])
        datatuple = tuple(Datalistname)

        print(Datalistname)

        for r in datatuple:
            Rescue.objects.filter(name=r).delete()
        return JsonResponse({'statue':'success delete'})
    else:
        return JsonResponse({'statue':'fail'})



    xname = {}
    xname['ggg'] = 'hhh'
    dump = json.dumps(xname)
    return JsonResponse({'foo':'bar'})
