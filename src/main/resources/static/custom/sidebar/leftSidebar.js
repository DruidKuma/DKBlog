/**
 * Created by DruidKuma on 7/12/16.
 */
angular.module("blogApp").directive('leftSidebar', function() {
    return {
        restrict: 'E',
        replace: true,
        templateUrl: '/custom/sidebar/left-sidebar.html',
        controller: ['$scope', '$location', '$rootScope', 'Country', function($scope, $location, $rootScope, Country) {
            $scope.currentState = $location.path();
            $.Sidemenu.init();

            $rootScope.$on('$routeChangeSuccess', function(e, current, pre) {
                $scope.currentState = $location.path();
            });

            $scope.initCountries = function() {
                Country.flags().then(function(response) {
                    $scope.availableCountries = response.data;
                });
            }

            $scope.initCountries();
        }]
    }
});