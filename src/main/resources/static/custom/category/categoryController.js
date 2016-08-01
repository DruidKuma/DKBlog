/**
 * Created by DruidKuma on 7/12/16.
 */
angular.module("blogApp")
    .controller('CategoryController',['$scope', 'Category', '$translatePartialLoader', '$translate', '$uibModal', 'Country', 'I18NService',
        function($scope, Category, $translatePartialLoader, $translate, $uibModal, Country, I18NService) {
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
                    I18NService: function() {
                        return I18NService;
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
    .controller('EditCategoryController', function ($scope, $uibModalInstance, Country, Category, I18NService, category) {

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

        $scope.findTranslationByLang = function(translations, lang) {
            if(!translations) return;
            return translations.find(function(element, index, array) {
                return element.lang == lang;
            });
        };

        $scope.$watchCollection('category.countries', function(newValue, oldValue) {
            if(newValue) {
                var newTranslations = [];
                angular.forEach($scope.category.countries, function(country) {
                    if(!$scope.findTranslationByLang(newTranslations, country.defaultLanguageIso)) {
                        var translation = $scope.findTranslationByLang($scope.category.translations, country.defaultLanguageIso);
                        if(translation) newTranslations.push(translation);
                        else {
                            I18NService.getForKeyAndLang({key: "components.category." + $scope.category.nameKey,
                                lang: country.defaultLanguageIso}).then(function(response) {
                                newTranslations.push(response.data);
                            });
                        }
                    }
                });
                $scope.category.translations = newTranslations;
            }
        });

        $scope.saveCategory = function () {
            $uibModalInstance.close($scope.category);
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    });
