'use strict';

angular.module('flightalertApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('permittedCarrier', {
                parent: 'entity',
                url: '/permittedCarriers',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'flightalertApp.permittedCarrier.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/permittedCarrier/permittedCarriers.html',
                        controller: 'PermittedCarrierController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('permittedCarrier');
                        $translatePartialLoader.addPart('carrierCode');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('permittedCarrier.detail', {
                parent: 'entity',
                url: '/permittedCarrier/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'flightalertApp.permittedCarrier.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/permittedCarrier/permittedCarrier-detail.html',
                        controller: 'PermittedCarrierDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('permittedCarrier');
                        $translatePartialLoader.addPart('carrierCode');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'PermittedCarrier', function($stateParams, PermittedCarrier) {
                        return PermittedCarrier.get({id : $stateParams.id});
                    }]
                }
            })
            .state('permittedCarrier.new', {
                parent: 'permittedCarrier',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/permittedCarrier/permittedCarrier-dialog.html',
                        controller: 'PermittedCarrierDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    carrierCode: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('permittedCarrier', null, { reload: true });
                    }, function() {
                        $state.go('permittedCarrier');
                    })
                }]
            })
            .state('permittedCarrier.edit', {
                parent: 'permittedCarrier',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/permittedCarrier/permittedCarrier-dialog.html',
                        controller: 'PermittedCarrierDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['PermittedCarrier', function(PermittedCarrier) {
                                return PermittedCarrier.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('permittedCarrier', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('permittedCarrier.delete', {
                parent: 'permittedCarrier',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/permittedCarrier/permittedCarrier-delete-dialog.html',
                        controller: 'PermittedCarrierDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['PermittedCarrier', function(PermittedCarrier) {
                                return PermittedCarrier.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('permittedCarrier', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
