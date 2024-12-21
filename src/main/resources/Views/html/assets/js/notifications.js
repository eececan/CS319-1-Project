
$(document).ready(function() {
    // Function to format timestamp
    function formatTime(timestamp) {
        return moment(timestamp).fromNow();
    }
    let notificationDropdown = $('.notifications-dropdown');

    // Toggle dropdown on click
    notificationDropdown.on('click', function(e) {
        e.preventDefault();
        loadNotifications();
        $(this).find('.dropdown-menu').toggle();
    });

    // Close dropdown when clicking outside
    $(document).on('click', function(e) {
        if (!$(e.target).closest('.notifications-dropdown').length) {
            $('.notifications-panel').hide();
        }
    })
    // Function to update notification count
    function updateNotificationCount() {
        $.get('/api/notifications/unread', function(notifications) {
            const count = notifications.length;
            $('.notification-count').text(count);

            if (count > 0) {
                $('.notification-count').show();
            } else {
                $('.notification-count').hide();
            }
        });
    }

    // Function to load notifications
    function loadNotifications() {
        $.get('/api/notifications/unread', function(notifications) {
            const notificationsList = $('.notifications-list');
            notificationsList.empty();

            notifications.forEach(notification => {
                const notificationHtml = `
                    <div class="notification-item unread" data-id="${notification.id}">
                        <div class="notification-content">
                            ${notification.message}
                        </div>
                        <div class="notification-time">
                            ${formatTime(notification.timestamp)}
                        </div>
                    </div>
                `;
                notificationsList.append(notificationHtml);
            });

            updateNotificationCount();
        });
    }

    // Mark notification as read when clicked
    $(document).on('click', '.notification-item', function() {
        const notificationId = $(this).data('id');
        $.post(`/api/notifications/${notificationId}/read`)
            .done(() => {
                $(this).removeClass('unread');
                updateNotificationCount();
            });
    });

    // Load notifications initially
    loadNotifications();

    // Update notifications every 30 seconds
    setInterval(loadNotifications, 30000);
});