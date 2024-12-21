$(document).ready(function() {
    // Function to format timestamp
    function formatTime(timestamp) {
        return moment(timestamp).fromNow();
    }
    let notificationDropdown = $('.notifications-dropdown');
    let notificationPanel = $('.notifications-panel');
    let isOpen = false;
    let lastNotificationCount = 0;
    // Function to play notification sound
    function playNotificationSound() {
        const audio = document.getElementById('notification-sound');
        if (audio) {
            audio.play().catch(function(error) {
                console.log("Sound play failed:", error);
            });
        }
    }


    // Toggle dropdown on click
    notificationDropdown.on('click', function(e) {
        e.preventDefault();
        e.stopPropagation();

        if (!isOpen) {
            loadNotifications();
            notificationPanel.show();
            isOpen = true;
        } else {
            notificationPanel.hide();
            isOpen = false;
        }
    });

    // Close dropdown when clicking outside
    $(document).on('click', function(e) {
        if (!$(e.target).closest('.notifications-dropdown').length) {
            notificationPanel.hide();
            isOpen = false;
        }
    });

    // Prevent dropdown from closing when clicking inside
    notificationPanel.on('click', function(e) {
        e.stopPropagation();
    });

    // Function to update notification count
    function updateNotificationCount() {
        $.get('/api/notifications/unread', function(notifications) {
            const count = notifications.length;
            $('.notification-count').text(count);

            // Play sound if new notifications arrived
            if (count > lastNotificationCount) {
                playNotificationSound();
            }
            lastNotificationCount = count;

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

            if (notifications.length === 0) {
                notificationsList.append(`
                    <div class="notification-item">
                        <div class="notification-content">
                            No new notifications
                        </div>
                    </div>
                `);
            } else {
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
            }

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