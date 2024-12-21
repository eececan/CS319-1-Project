$(document).ready(function() {
    function formatTime(timestamp) {
        return moment(timestamp).fromNow();
    }

    let notificationDropdown = $('.notifications-dropdown');
    let notificationPanel = $('.notifications-panel');
    let isOpen = false;
    let lastNotificationCount = 0;

    function playNotificationSound() {
        const audio = document.getElementById('notification-sound');
        if (audio) {
            audio.play().catch(function(error) {
                console.log("Sound play failed:", error);
            });
        }
    }

    function updateNotificationCount() {
        $.get('/api/notifications/unread', function(notifications) {
            const count = notifications.length;
            $('.notification-count').text(count);

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

    function loadNotifications() {
        $.get('/api/notifications/unread', function(notifications) {
            const notificationsList = $('.notifications-list');
            notificationsList.empty();

            if (notifications.length === 0) {
                notificationsList.append(`
                    <div class="notification-item">
                        <div class="notification-content">No new notifications</div>
                    </div>
                `);
            } else {
                notifications.forEach(notification => {
                    const notificationHtml = `
                        <div class="notification-item unread" data-id="${notification.id}">
                            <input type="checkbox" class="mark-as-read" />
                            <div class="notification-content">
                                ${notification.message}
                                <div class="notification-time">${formatTime(notification.timestamp)}</div>
                            </div>
                        </div>
                    `;
                    notificationsList.append(notificationHtml);
                });
            }

            updateNotificationCount();
        });
    }

    $(document).on('change', '.mark-as-read', function() {
        const $item = $(this).closest('.notification-item');
        const notificationId = $item.data('id');

        $.post(`/api/notifications/${notificationId}/read`)
            .done(function() {
                $item.removeClass('unread');
                updateNotificationCount();
            })
            .fail(function(error) {
                console.error('Error marking notification as read:', error);
            });
    });

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

    $(document).on('click', function(e) {
        if (!$(e.target).closest('.notifications-dropdown').length) {
            notificationPanel.hide();
            isOpen = false;
        }
    });

    notificationPanel.on('click', function(e) {
        e.stopPropagation();
    });

    loadNotifications();
    setInterval(loadNotifications, 30000);
});