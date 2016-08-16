/**
 * Created by DruidKuma on 8/15/16.
 */
angular.module("blogApp")
    .controller('SystemPropertiesController',['$scope', 'Property', 'SweetAlert', function($scope, Property, SweetAlert) {
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
                delete $scope.inserted;
                $scope.loadProperties();
            });
        };

        $scope.removeProperty = function(property) {
            SweetAlert.swal({
                    title: "Are you sure?",
                    text: "This action will delete this property and can influence the work of the whole application!",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "Yes, delete it!",
                    closeOnConfirm: true},
                function(){
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

        $scope.loadProperties();
    }]);