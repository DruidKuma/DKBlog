/**
 * Created by DruidKuma on 8/15/16.
 */
angular.module("blogApp")
    .controller('SystemPropertiesController',['$scope', 'Property', 'SweetAlert', function($scope, Property, SweetAlert) {
        //Page Heading
        $scope.$on('$routeChangeSuccess', function () {
            $scope.pageHeading.title = "System Properties";
        });
        $scope.$on('countryChanged', function(event, data) {
            $scope.loadProperties();
        });

        $scope.loadProperties = function() {
            $scope.loadingProcess = true;
            Property.all().then(function(response) {
                $scope.allProperties = response.data;
            }).finally(function() {
                $scope.loadingProcess = false;
            });
        };

        $scope.saveProperty = function(data, property) {
            Property.save(
                {
                    id: property.id,
                    key: data.key,
                    value: data.value,
                    lastModified: new Date(),
                    country: property.country
                }
            ).then(function(response) {
                delete $scope.inserted;
                $scope.loadProperties();
            }, function(error) {$scope.showError()});
        };

        $scope.removeProperty = function(property) {
            $scope.executeWithWarning("Are you sure?",
                "This action will delete this property and can influence the work of the whole application!",
                "Yes, delete it!",
                function() {
                    Property.delete(property).then(function(response) {
                        $scope.loadProperties();
                    }, function(error) {
                        $scope.showError();
                    });
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

        $scope.makeDefault = function(property) {
            Property.getDefault(property.key).then(function(response) {
                var defaultProperty = response.data;
                if(!defaultProperty) {
                    Property.makeDefault(property.id).then(function(response) {
                        $scope.loadProperties();
                    }, function(error) {$scope.showError()});
                }
                else {
                    $scope.executeWithWarning("Are you sure?",
                        "This action will override the default value for this property: " + defaultProperty.value,
                        "Yes, override it!",
                        function() {
                            Property.makeDefault(property.id).then(function(response) {
                                $scope.loadProperties();
                            }, function(error) {$scope.showError()});
                        });
                }
            }, function(error) {$scope.showError()});
        };

        $scope.copyForCountry = function(property) {
            Property.createCopyForCountry(property).then(function(reponse) {
                $scope.loadProperties();
            }, function(error) {$scope.showError()})
        };

        $scope.loadProperties();
    }]);