/**
 * Created by DruidKuma on 8/15/16.
 */
angular.module("blogApp")
    .controller('SystemPropertiesController',['$scope', 'Property', function($scope, Property) {
        //Page Heading
        $scope.$on('$routeChangeSuccess', function () {
            $scope.pageHeading.title = "System Properties";
        });

        $scope.loadProperties = function() {
            $scope.loadingProcess = true;
            Property.all().then(function(response) {
                $scope.allProperties = response.data;
            }).finally(function() {
                $scope.loadingProcess = false;
            });
        };

        $scope.saveProperty = function(data, id) {
           console.log(data);
        };

        $scope.removeProperty = function(property) {
            console.log(property);
        };

        $scope.loadProperties();
    }]);