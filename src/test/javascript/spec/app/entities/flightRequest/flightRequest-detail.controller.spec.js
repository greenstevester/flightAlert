'use strict';

describe('Controller Tests', function() {

    describe('FlightRequest Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockFlightRequest, MockFlightSlice;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockFlightRequest = jasmine.createSpy('MockFlightRequest');
            MockFlightSlice = jasmine.createSpy('MockFlightSlice');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'FlightRequest': MockFlightRequest,
                'FlightSlice': MockFlightSlice
            };
            createController = function() {
                $injector.get('$controller')("FlightRequestDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'flightalertApp:flightRequestUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
