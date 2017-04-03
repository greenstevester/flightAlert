 'use strict';

angular.module('flightalertApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-flightalertApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-flightalertApp-params')});
                }
                return response;
            }
        };
    });
