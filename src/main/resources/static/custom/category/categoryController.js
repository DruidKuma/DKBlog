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

        $scope.items = ['item1', 'item2', 'item3'];

        $scope.openEditModal = function(category) {
            var modalInstance = $uibModal.open({
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
        };

        $scope.$on('countryChanged', function(event, data) {
            $scope.loadCategories();
        });

        $scope.loadCategories();
    }])
    .controller('EditCategoryController', function ($scope, $uibModalInstance, category) {

    $scope.category = category;

    $scope.ok = function () {
        $uibModalInstance.close('OK');
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
});
