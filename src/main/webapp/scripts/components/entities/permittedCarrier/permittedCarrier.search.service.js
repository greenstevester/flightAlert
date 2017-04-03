'use strict';

angular.module('flightalertApp')
    .factory('PermittedCarrierSearch', function ($resource) {
        return $resource('api/_search/permittedCarriers/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
