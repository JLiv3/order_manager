let app = angular.module("ordersManagerment", ['ngAnimate', 'ngSanitize', 'ui.bootstrap']);

app.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            let model = $parse(attrs.fileModel);
            let modelSetter = model.assign;

            element.bind('change', function () {
                scope.$apply(function () {
                    // for (let i = 0; i < element[0].files.length; i++) {
                    //     modelSetter(scope[i], element[0].files[i]);
                    // }
                    modelSetter(scope, element[0].files);
                });
            });
        }
    };
}]);

app.controller("ordersController", function ($scope, $http) {
    $scope.orders = [];
    $scope.seacrhFilter = {
        code: "",
        checked: "all"

    };
    $scope.orderForm = {
        type: false,
        id: 0,
        code: "",
        name: "",
        note: "",
        files: []
    };
    $scope.currentPage = 1;
    $scope.maxSize = 5;
    $scope.totalItems = 0;
    $scope.itemsPerPage = 30;
    $scope.filteredOrders = [];
    $scope.hidePagination = false;

    function _refreshOrdersData() {
        $http({
            method: 'GET',
            url: '/api/orders'
        }).then(
            function (res) { // success
                $scope.orders = res.data;
                if ($scope.seacrhFilter.code != "") {
                    $scope.orders = $scope.orders.filter(o => o.code.includes($scope.seacrhFilter.code));
                }
                if ($scope.seacrhFilter.checked == "checked") {
                    $scope.orders = $scope.orders.filter(o => o.checked);
                } else if ($scope.seacrhFilter.checked == "unchecked") {
                    $scope.orders = $scope.orders.filter(o => !o.checked);
                }
                $scope.totalItems = $scope.orders.length;
                $scope.hidePagination = $scope.totalItems < $scope.itemsPerPage;
                $scope.filteredOrders = $scope.orders.slice(0, $scope.itemsPerPage);
            },
            function (res) { // error
                console.log("Error: " + res.status + " : " + res.data);
            }
        );
    }

    // Now load the data from server
    _refreshOrdersData();

    $scope.pageChanged = function () {
        let begin = ($scope.currentPage - 1) * $scope.itemsPerPage;
        $scope.filteredOrders = $scope.orders.slice(begin, begin + $scope.itemsPerPage);
    };

    $scope.editOrder = function (o) {
        $scope.orderForm.type = true;
        $scope.orderForm.id = o.id;
        $scope.orderForm.code = o.code;
        $scope.orderForm.name = o.name;
        $scope.orderForm.note = o.note;
        $scope.orderForm.files = [];
        clearInputFile();
    };

    $scope.clearData = function () {
        _clearFormData()
    };

    $scope.submitOrder = function () {
        let url = "/api/orders";
        const data = new FormData();
        data.append("id", $scope.orderForm.id);
        data.append("code", $scope.orderForm.code.trim());
        data.append("name", $scope.orderForm.name.trim());
        data.append("note", $scope.orderForm.note.trim());
        for (let i = 0; i < $scope.orderForm.files.length; i++) {
            data.append("files", $scope.orderForm.files[i]);
        }
        let config = {
            transformRequest: angular.identity,
            transformResponse: angular.identity,
            headers: {
                'Content-Type': undefined
            }
        };
        if ($scope.orderForm.type) {
            $http.put(url, data, config).then(_success, _error);
        } else {
            $http.post(url, data, config).then(_success, _error);
        }
    };

    function _success(res) {
        $("#modalOrder").modal("hide");
        _refreshOrdersData();
        _clearFormData();
        alert("Thành công!!!");
    }

    function _error(res) {
        alert("Lỗi: " + res.data.message);
    }

    function _clearFormData() {
        $scope.orderForm.type = false;
        $scope.orderForm.id = 0;
        $scope.orderForm.code = "";
        $scope.orderForm.name = "";
        $scope.orderForm.note = "";
        $scope.orderForm.files = [];
        clearInputFile();
    }

    $scope.deleteOrder = function (o) {
        if (confirm("Bạn có chắc chắn muốn xóa đơn hàng này ?")) {
            $http({
                method: 'DELETE',
                url: '/api/orders/' + o.id
            }).then(_success, _error);
        }
    };

    $scope.toggleChecked = function (order) {
        $http({
            method: "PUT",
            url: "/api/orders/toggleChecked",
            data: angular.toJson(order),
            headers: {
                'Content-Type': 'application/json'
            }
        })
        // .then(_success, _error);
    };

    $scope.search = function () {
        _refreshOrdersData();
    };

    $scope.loadListImg = function (o) {
        let e = document.getElementById(o.id + o.code);
        o.listImg.forEach(
            img => {
                e.innerHTML += '<img class="img-thumbnail img-thumbnail-inTable" src="/api/orders/image/' + o.id + '/' + img.shortName + '"/>';
            }
        )
    };

    $scope.showImg = function (imgs, id) {
        let ca = document.getElementById("carousel-indicators");
        let ci = document.getElementById("carousel-inner");
        ca.innerHTML = '';
        ci.innerHTML = '';
        imgs.forEach((e) => {
            ca.innerHTML += '<li data-slide-to="4" data-target="#img-show"></li>';
            ci.innerHTML += '<div class="carousel-item"><img class="img-thumbnail" src="/api/orders/image/' + id + '/' + e.shortName + '"/></div>';
        });
        ca.firstChild.classList.add("active");
        ci.firstChild.classList.add("active");
    };
})
