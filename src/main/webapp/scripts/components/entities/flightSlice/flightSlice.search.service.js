'use strict';

angular.module('flightalertApp')
    .factory('FlightSliceSearch', function ($resource) {
        return $resource('api/_search/flightSlices/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
