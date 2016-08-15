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
            Property.save(
                {
                    id: id,
                    key: data.key,
                    value: data.value,
                    lastModified: new Date(),
                    country: $scope.currentCountry
                }
            ).then(function(response) {
                $scope.loadProperties();
            });
        };

        $scope.removeProperty = function(property) {
            Property.delete(property).then(function(response) {
                $scope.loadProperties();
            });
        };

        $scope.addNewProperty = function() {
            $scope.inserted = {
                country: $scope.currentCountry,
                key: '',
                value: '',
                lastModified: new Date(),
                new: true
            };
            $scope.allProperties.push($scope.inserted);
        };

        $scope.cancelFormEditing = function(form) {
            form.$cancel();
            if($scope.inserted) {
                $scope.allProperties.pop();
            }
            delete $scope.inserted;
        };

        $scope.loadProperties();
    }]);