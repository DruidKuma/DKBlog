/**
 * Created by DruidKuma on 7/12/16.
 */
angular.module("blogApp")
    .controller('CategoryController',['$scope', 'Category', '$translatePartialLoader', '$translate', '$uibModal', function($scope, Category, $translatePartialLoader, $translate, $uibModal) {
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
    .controller('EditCategoryController', function ($scope, $uibModalInstance, category) {

        $scope.category = category;

        $scope.colorPickerOptions = {format: 'hex'};

        $scope.saveCategory = function () {
            $uibModalInstance.close($scope.category);
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    });
