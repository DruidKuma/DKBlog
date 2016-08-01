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
    .controller('EditCategoryController', function ($scope, $uibModalInstance, Country, category) {

        $scope.category = category ? category : {};

        $scope.category.chosenCountries = [
            { name: "America", isoCode: "US" },
            { name: "Russia", isoCode: "RU" }
        ];
        $scope.category.translations = [
            { lang: 'ru', display: 'Russian', value: 'Типсы и трики'},
            { lang: 'en', display: 'English', value: 'Tips & Tricks'}
        ];


        $scope.loadCountries = function($query) {
            return Country.flags().then(function(response) {
                var countries = response.data;
                return countries.filter(function(country) {
                    return country.name.toLowerCase().indexOf($query.toLowerCase()) != -1;
                });
            });
        };

        $scope.$watchCollection('category.chosenCountries', function(oldValue, newValue) {
            console.log('pizda');
        });

        $scope.colorPickerOptions = {format: 'hex'};

        $scope.saveCategory = function () {
            $uibModalInstance.close($scope.category);
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    });
