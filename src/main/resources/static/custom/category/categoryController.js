/**
 * Created by DruidKuma on 7/12/16.
 */
angular.module("blogApp")
    .controller('CategoryController',['$scope', 'Category', '$translatePartialLoader', '$translate', '$uibModal', 'Country', function($scope, Category, $translatePartialLoader, $translate, $uibModal, Country) {
        //Page Heading
        $scope.$on('$routeChangeSuccess', function () {
            $scope.pageHeading.title = "Category Overview";
            $translatePartialLoader.addPart('components.category');
            $translate.refresh();
        });

        $scope.loadCategories = function() {
            $scope.loadingProcess = true;
            Category.all().then(function(response) {
                $scope.categories = response.data;
            }).finally(function() {
                $scope.loadingProcess = false;
            });
        };

        $scope.openEditModal = function(category) {
            var editCategoryModal = $uibModal.open({
                animation: true,
                templateUrl: 'editCategoryModal.html',
                controller: 'EditCategoryController',
                size: 'lg',
                resolve: {
                    Country: function() {
                        return Country;
                    },
                    Category: function() {
                        return Category;
                    },
                    category: function () {
                        return category;
                    }
                }
            });

            editCategoryModal.result.then(function (editedCategory) {
                // save edited category
            });

        };

        $scope.$on('countryChanged', function(event, data) {
            $scope.loadCategories();
        });

        $scope.loadCategories();
    }])
    .controller('EditCategoryController', function ($scope, $uibModalInstance, Country, Category, category) {

        $scope.colorPickerOptions = {format: 'hex'};

        if(category) {
            Category.one(category.id).then(function(response) {
                $scope.category = response.data;
            });
        }
        else $scope.category = {};

        $scope.loadCountries = function($query) {
            return Country.flags().then(function(response) {
                var countries = response.data;
                return countries.filter(function(country) {
                    return country.name.toLowerCase().indexOf($query.toLowerCase()) != -1;
                });
            });
        };

        $scope.$watchCollection('category.chosenCountries', function(oldValue, newValue) {

        });

        $scope.saveCategory = function () {
            $uibModalInstance.close($scope.category);
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    });
