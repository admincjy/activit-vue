/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
'use strict';

angular.module('activitiModeler').service('UserService', ['$http', '$q',
    function ($http, $q) {

        var httpAsPromise = function(options) {
            var deferred = $q.defer();
            $http(options).
                success(function (response, status, headers, config) {
                    deferred.resolve(response);
                })
                .error(function (response, status, headers, config) {
                    deferred.reject(response);
                });
            return deferred.promise;
        };

        /*
         * Filter users based on a filter text.
         */
        this.getFilteredUsers = function (filterText, taskId, processInstanceId) {

            var filterUrl =  '/pmtapi/Wf_Identity/filteUser?filter=';

            if(filterText){
                filterUrl +=filterText;
            }
            return httpAsPromise({
                method: 'GET',
                url: filterUrl,
            });
        };

    }]);

angular.module('activitiModeler').service('GroupService', ['$http', '$q',
    function ($http, $q) {

        var httpAsPromise = function(options) {
            var deferred = $q.defer();
            $http(options).
                success(function (response, status, headers, config) {
                    deferred.resolve(response);
                })
                .error(function (response, status, headers, config) {
                    deferred.reject(response);
                });
            return deferred.promise;
        };

        /*
         * Filter functional groups based on a filter text.
         */
        this.getFilteredGroups = function (filterText) {

            var filterUrl =  '/pmtapi/Wf_Identity/filteGroup?filter=';

            if(filterText){
                filterUrl +=filterText;
            }

            return httpAsPromise({
                method: 'GET',
                url: filterUrl,
            });
        };
    }]);
