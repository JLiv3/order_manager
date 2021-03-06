var app = angular.module("usersManagerment", []);
app.controller("usersController", function ($scope, $http) {
    $scope.users = [];
    $scope.userFrom = {
        type: false,
        id: 0,
        username: "",
        password: "",
        role: "USER"
    };
    // Now load the data from server
    _refreshUsersData();

    function _refreshUsersData() {
        $http({
            method: 'GET',
            url: '/api/users'
        }).then(
            function (res) { // success
                $scope.users = res.data;
            },
            function (res) { // error
                console.log("Error: " + res.status + " : " + res.data);
            }
        );
    }

    $scope.editUser = function (u) {
        $scope.userFrom.type = true;
        $scope.userFrom.id = u.id;
        $scope.userFrom.username = u.username;
        $scope.userFrom.role = u.role;
    }

    $scope.deleteUser = function (u) {
        if (confirm("Bạn có chắc chắn muốn xóa người dùng này ?")) {
            $http({
                method: 'DELETE',
                url: '/api/users/' + u.id
            }).then(_success, _error);
        }
    }

    $scope.submitUsers = function () {
        let method = "";
        if (!$scope.userFrom.type) {
            method = "POST";
            if ($scope.userFrom.password === "") {
                $scope.userFrom.password = $scope.userFrom.username;
            }
        } else {
            method = "PUT";
        }
        $http({
            method: method,
            url: "/api/users",
            data: angular.toJson($scope.userFrom),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(_success, _error);
    }

    $scope.clearData = function () {
        _clearFormData()
    }

    function _success(res) {
        $("#modalUser").modal("hide");
        _refreshUsersData();
        _clearFormData();
        alert("Thành công !!!");
    }

    function _error(res) {
        // console.log(res)
        alert("Lỗi: " + res.data.message);
    }

    function _clearFormData() {
        $scope.userFrom.type = false;
        $scope.userFrom.id = 0;
        $scope.userFrom.username = "";
        $scope.userFrom.password = "";
        $scope.userFrom.role = "USER";
    }

});