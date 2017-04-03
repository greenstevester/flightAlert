'use strict';

angular.module('flightalertApp')
    .factory('FlightRequestSearch', function ($resource) {
        return $resource('api/_search/flightRequests/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
