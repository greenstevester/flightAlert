'use strict';

angular.module('flightalertApp')
    .factory('Passenger', function ($resource, DateUtils) {
        return $resource('api/passengers/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
