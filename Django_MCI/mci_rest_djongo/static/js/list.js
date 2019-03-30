$(function (){
  var $bt_add = $('#btn_add'),
    $bt_delete = $('#btn_delete'),
    $bt_edit = $('#btn_edit'),
    $mytable = $('#table');
  window.act = {
    "click #like":function(e, value, row, index) {
      console.log("henrt")
    }

  };
  // var oButtonInit = new ButtonInit();
  // oButtonInit.Init();
  $("#table").bootstrapTable({
    method: 'post',
    striped: true,
    toolbar: "#toolbar",
    search: true,
    showRefresh: true,
    showToggle: true,
    showColumns: true,
    detailview: true,
    cardView: false,
    detailView: false,
    // clickToSelect: true,
    height: $(window).height() - 110,
    width: $(window).width(),
    minimumCountColumns: 2,
    showPaginationSwitch: true,
    queryParams: queryParams,
    pagination: true,
    pagelist: [10, 25, 50],
    sidePagination: "server",
    url: '/queryRescue/',
    responsehandler: responseHandler,
    // rowStyle:rowStyle,
    showExport: true,
    exportDataType: 'all',
    columns: [{
        checkbox: true
      },

      {
        field: '',
        title: 'Sort No.',
        formatter: function(value, row, index) {
          return index + 1;
        }
      },
      {
        field: 'name',
        title: 'Name',
        align: 'center',
        valign: 'middle',
        sortable: true
      },
      {
        field: 'commander',
        title: 'Commander',
        align: 'center',
        valign: 'middle',
        sortable: true,
        // cellStyle: function(value){
        //         if(value=='1'){
        //             return { classes: 'success' };
        //         }else{
        //             return { classes: 'danger' };
        //         }
        //     },

      },
      {
        field: 'lat',
        title: 'Lat',
        align: 'center',
        valign: 'middle',
        sortable: true,
      },
      {
        field: 'lon',
        title: 'Lon',
        align: 'center',
        valign: 'middle',
        sortable: true,
      },
      {
        field: 'timestart',
        title: 'TimeStart',
        align: 'center',
        valign: 'middle',
        sortable: true,
      },
      {
        field: '',
        title: 'Look Device',
        align: 'center',
        valign: 'middle',
        events: act,
        formatter: nameFormatter,


      },
    ]
  });

  $bt_add.click(function() {
    window.open('http://127.0.0.1:8000/easyRescue', 'Add', config = 'height=500,width=600');
  });
  $bt_delete.click(function() {
    var mydata = JSON.stringify($mytable.bootstrapTable('getSelections'));

    // let user = JSON.parse({'data':'me'});
    // console.log(user)
    // var namearray = []
    // for (i = 0; i < mydata.length; i++)
    // {
    //   console.log(mydata[i])
    // }
    $.post("http://127.0.0.1:8000/deleteRescue/", {
      'mydata': mydata
    }, function(data, status) {
      alert("Data: " + data.statue);
    });

    $("#table").bootstrapTable('refresh');
    // alert('getSelections: ' + JSON.stringify($mytable.bootstrapTable('getSelections')));
  });
  $bt_edit.click(function() {
    if ($mytable.bootstrapTable('getSelections').length == 1) {

      var name = $mytable.bootstrapTable('getSelections')[0]['name']
      var url = 'http://127.0.0.1:8000/easyEditRescue/' + name + '/'
      console.log(url)
      window.open(url, 'Edit', config = 'height=500,width=600');

    } else {
      alert('Please Just one');
    }
    // alert($mytable.bootstrapTable('getSelections').length)
    // window.open('http://127.0.0.1:8000/easyRescue', 'Add', config='height=500,width=600');
  });



  function nameFormatter(value, row, index) {
    return [
      '<button id="like" type="button" class ="btn btn-default">查看</button> &nbsp;&nbsp;'


      // '<a class="remove" href="javascript:void(0)" title="Remove">',
      //       '<i class="glyphicon glyphicon-remove"></i>',
      //       '</a>'
    ].join("")
  }

  function queryParams(params) {
    return { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
      limit: params.limit, //页面大小
      offset: params.offset //页码
      //name: $("#txt_name").val()//关键字查询
    };
  }
  var ButtonInit = function() {
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function() {
      //初始化页面上面的按钮事件
    };

    return oInit;
  };

  function responseHandler(res) {
    var resultStr = $.parseJSON(res);
    $.each(resultStr.Items, function(i, row) {
      row.operate = "";
      row.Id = row.RoleId;
    });
    if (res) {
      return {
        "rows": res.result,
        "total": res.totalCount
      };
    } else {
      return {
        "rows": [],
        "total": 0
      };
    }
  }

});
