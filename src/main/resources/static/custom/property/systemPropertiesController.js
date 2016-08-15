/**
 * Created by DruidKuma on 8/15/16.
 */
angular.module("blogApp")
    .controller('SystemPropertiesController',['$scope', 'Property', function($scope, Property) {
        //Page Heading
        $scope.$on('$routeChangeSuccess', function () {
            $scope.pageHeading.title = "System Properties";
        });


    }]);