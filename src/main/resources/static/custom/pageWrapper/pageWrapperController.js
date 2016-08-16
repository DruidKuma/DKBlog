/**
 * Created by DruidKuma on 7/20/16.
 */
angular.module("blogApp")
    .controller('PageWrapperController',['$scope', '$location', 'BlogEntry', '$cookies', 'SweetAlert', function($scope, $location, BlogEntry, $cookies, SweetAlert) {

        var currentCountryIso = $cookies.get('currentCountryIso');
        if(!currentCountryIso) $cookies.put('currentCountryIso', 'US');
        $scope.currentCountry = {
            isoCode: $cookies.get('currentCountryIso')
        };

        $scope.pageHeading = {};
        $scope.blogListFilter = {
            search: '',
            filterChanged: false
        };

        $scope.reloadBlogPosts = function() {
            $scope.loadingProcess = true;
            BlogEntry.page($scope.blogListFilter).then(function(response) {
                $scope.posts = response.data.content;
                $scope.blogListFilter.totalItems = response.data.totalElements;
            }).finally(function() {
                $scope.loadingProcess = false;
            });
        };
        $scope.reloadForSearch = function() {
            if($location.path() != '/list') $location.path('/list').search({partial: true});
            $scope.blogListFilter.filterChanged = true;
            $scope.reloadBlogPosts();
        };

        $scope.showError = function() {
            SweetAlert.swal({
                title: "Error!",
                text: "Something went wrong...",
                imageUrl: "/dist/images/error.gif" });
        };

        $scope.executeWithWarning = function(titleText, message, buttonText, callback) {
            SweetAlert.swal({
                    title: titleText,
                    text: message,
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: buttonText,
                    closeOnConfirm: true},
                function(isConfirm){
                    if(isConfirm) {
                        callback();
                    }
                });
        };
    }]);
