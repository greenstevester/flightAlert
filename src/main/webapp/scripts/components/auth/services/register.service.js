'use strict';

angular.module('flightalertApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


