(function() {
    'use strict';

    angular
        .module('attendanceApp')
        .controller('AttendeeDeleteController',AttendeeDeleteController);

    AttendeeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Attendee'];

    function AttendeeDeleteController($uibModalInstance, entity, Attendee) {
        var vm = this;

        vm.attendee = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Attendee.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
