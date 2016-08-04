/**
 * Created by DruidKuma on 8/2/16.
 */
angular.module("blogApp").service('Media', ["$http", function($http) {
    return {
        page: function(params) {
            return $http({
                url: BASE_URL + "/api/blog/image/page",
                method: "get",
                params: params
            })
        }
    }
}]);