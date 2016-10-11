(function() {
    'use strict';
    angular
        .module('attendanceApp')
        .factory('Attendee', Attendee);

    Attendee.$inject = ['$resource'];

    function Attendee ($resource) {
        var resourceUrl =  'api/attendees/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
