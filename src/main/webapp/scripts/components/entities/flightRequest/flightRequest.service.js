'use strict';

angular.module('flightalertApp')
    .factory('FlightRequest', function ($resource, DateUtils) {
        return $resource('api/flightRequests/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.validFromDate = DateUtils.convertDateTimeFromServer(data.validFromDate);
                    data.validToDate = DateUtils.convertDateTimeFromServer(data.validToDate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
