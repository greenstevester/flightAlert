'use strict';

angular.module('flightalertApp')
    .factory('PassengerSearch', function ($resource) {
        return $resource('api/_search/passengers/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
