var app = angular.module("ordersManagerment", []);

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

    $scope.orderForm = {
        type: false,
        id: 0,
        code: "",
        name: "",
        note: "",
        files: []
    };

    $scope.seacrhFilter = {};

    // Now load the data from server
    _refreshOrdersData();

    function _refreshOrdersData() {
        $http({
            method: 'GET',
            url: '/api/orders'
        }).then(
            function (res) { // success
                $scope.orders = res.data;
            },
            function (res) { // error
                console.log("Error: " + res.status + " : " + res.data);
            }
        );
    }

    $scope.editOrder = function (o) {
        $scope.orderForm.type = true;
        $scope.orderForm.id = o.id;
        $scope.orderForm.code = o.code;
        $scope.orderForm.name = o.name;
        $scope.orderForm.note = o.note;
        $scope.orderForm.files = [];
        clearInputFile();
    }

    $scope.clearData = function () {
        _clearFormData()
    }

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
    }

    function _success(res) {
        $("#modelOrder").modal("hide");
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

    $scope.countSortCheck = 0;

    $scope.sortChecked = function () {
        if ($scope.countSortCheck % 3 == 0) {
            $scope.seacrhFilter.checked = true;
            $scope.countSortCheck++;
        } else if ($scope.countSortCheck % 3 == 1) {
            $scope.seacrhFilter.checked = false;
            $scope.countSortCheck++;
        } else {
            delete $scope.seacrhFilter.checked;
            $scope.countSortCheck++;
        }
    }

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
    }

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
    }
})
