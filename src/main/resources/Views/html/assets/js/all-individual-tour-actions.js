function handleIndividualTourApproval(buttonElement) {
    // Extract data from the button's data-* attributes
    const individualTourId = buttonElement.getAttribute("data-individual-tour-id");
    const studentName = buttonElement.getAttribute("data-student-name");
    const tourDate = buttonElement.getAttribute("data-tour-date");

    // Reference to the modal and its elements
    const confirmModal = new bootstrap.Modal(document.getElementById('confirmModal'));
    const confirmMessage = document.getElementById('confirmMessage');
    const confirmButton = document.getElementById('confirmButton');
    const cancelButton = document.querySelector('.btn-secondary[data-bs-dismiss="modal"]');
    const closeModalLabelButton = document.querySelector('.btn-close[data-bs-dismiss="modal"]');

    // Set the modal message dynamically
    confirmMessage.textContent = `Do you want to approve the individual tour for ${studentName} on ${tourDate}? Note that the student will be notified and this action cannot be undone.`;

    // Remove any previous event listeners to avoid conflicts
    confirmButton.replaceWith(confirmButton.cloneNode(true));
    const newConfirmButton = document.getElementById('confirmButton');

    // Attach the specific approval logic to the "Yes" button
    newConfirmButton.addEventListener('click', function () {
        // Call the individual tour approval API
        fetch(`/api/individual-tours/${individualTourId}/advisor/approve`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
        })
            .then((response) => {
                if (response.ok) {
                    // Show success notification
                    showNotification(`Individual tour for ${studentName} on ${tourDate} approved successfully!`, "success");

                    // Optionally reload the page to reflect the changes
                    setTimeout(() => location.reload(), 2000);
                } else {
                    return response.text().then((message) => {
                        throw new Error(message);
                    });
                }
            })
            .catch((error) => {
                console.error("Error approving individual tour:", error);

                // Show error notification
                showNotification(`Error: ${error.message}`, "error");
            })
            .finally(() => {
                confirmModal.hide();
            });
    });

    // Open the modal
    confirmModal.show();

    cancelButton.onclick = () => {
        confirmModal.hide();
    };

    closeModalLabelButton.onclick = () => {
        confirmModal.hide();
    };
}

function handleIndividualTourRejection(buttonElement) {
    // Extract data from the button's data-* attributes
    const individualTourId = buttonElement.getAttribute("data-individual-tour-id");
    const studentName = buttonElement.getAttribute("data-student-name");
    const tourDate = buttonElement.getAttribute("data-tour-date");

    // Reference to the modal and its elements
    const confirmModal = new bootstrap.Modal(document.getElementById('confirmModal'));
    const confirmMessage = document.getElementById('confirmMessage');
    const confirmButton = document.getElementById('confirmButton');
    const cancelButton = document.querySelector('.btn-secondary[data-bs-dismiss="modal"]');
    const closeModalLabelButton = document.querySelector('.btn-close[data-bs-dismiss="modal"]');

    // Set the modal message dynamically
    confirmMessage.textContent = `Do you want to reject the individual tour for ${studentName} on ${tourDate}? Note that the student will be notified and this action cannot be undone.`;

    // Remove any previous event listeners to avoid conflicts
    confirmButton.replaceWith(confirmButton.cloneNode(true));
    const newConfirmButton = document.getElementById('confirmButton');

    // Attach the specific rejection logic to the "Yes" button
    newConfirmButton.addEventListener('click', function () {
        // Call the individual tour rejection API
        fetch(`/api/individual-tours/${individualTourId}/advisor/reject`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
        })
            .then((response) => {
                if (response.ok) {
                    // Show success notification
                    showNotification(`Individual tour for ${studentName} on ${tourDate} rejected successfully!`, "success");

                    // Optionally reload the page to reflect the changes
                    setTimeout(() => location.reload(), 2000);
                } else {
                    return response.text().then((message) => {
                        throw new Error(message);
                    });
                }
            })
            .catch((error) => {
                console.error("Error rejecting individual tour:", error);

                // Show error notification
                showNotification(`Error: ${error.message}`, "error");
            })
            .finally(() => {
                confirmModal.hide();
            });
    });

    // Open the modal
    confirmModal.show();

    cancelButton.onclick = () => {
        confirmModal.hide();
    };

    closeModalLabelButton.onclick = () => {
        confirmModal.hide();
    };
}

document.addEventListener('DOMContentLoaded', function () {
    const guideDropdowns = document.querySelectorAll('.individual-guide-dropdown');
    let selectedTourId = null;
    let selectedGuideId = null;
    let selectedDropdown = null; // Keep track of the specific dropdown

    const confirmModal = new bootstrap.Modal(document.getElementById('confirmModal'));
    const confirmMessage = document.getElementById('confirmMessage');
    const confirmButton = document.getElementById('confirmButton');
    const cancelButton = document.querySelector('.btn-secondary[data-bs-dismiss="modal"]');
    const closeModalLabelButton = document.querySelector('.btn-close[data-bs-dismiss="modal"]');

    // Handle dropdown change and show confirmation modal
    guideDropdowns.forEach((dropdown) => {
        dropdown.addEventListener('change', function () {
            selectedTourId = this.getAttribute('data-tour-id'); // Extract individual tour ID from dropdown data attribute
            selectedGuideId = this.value; // Get the selected guideId
            selectedDropdown = this; // Keep a reference to the specific dropdown

            const studentName = this.dataset.studentname;
            const tourDate = this.dataset.tourdate;
            const selectedOptionText = this.options[this.selectedIndex].text;

            confirmMessage.textContent = `You are about to assign guide "${selectedOptionText}" to the individual tour for ${studentName} on ${tourDate}. Are you sure?`;
            confirmModal.show();
        });
    });

    // Handle Yes button click
    confirmButton.addEventListener('click', function () {
        if (selectedTourId && selectedGuideId) {
            fetch(`/api/individual-tours/${selectedTourId}/assign-guide`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ guideId: selectedGuideId }),
            })
                .then((response) => {
                    if (response.ok) {
                        // Show success notification
                        showNotification('Guide assigned successfully!', 'success');
                        setTimeout(() => location.reload(), 2000);
                    } else {
                        return response.json().then((json) => {
                            throw new Error(json.error || 'An unknown error occurred.');
                        });
                    }
                })
                .catch((error) => {
                    console.error('Error assigning guide:', error);

                    // Show backend error notification
                    showNotification(`Error: ${error.message}`, 'error');
                    resetDropdown(); // Reset dropdown in case of error
                })
                .finally(() => {
                    resetSelections();
                });

            confirmModal.hide();
        }
    });

    // Handle No button click
    cancelButton.addEventListener('click', function () {
        resetDropdown();
        resetSelections();
    });

    // Handle Close (X) button click
    closeModalLabelButton.addEventListener('click', function () {
        resetDropdown();
        resetSelections();
    });

    // Reset the dropdown to its initial state
    function resetDropdown() {
        if (selectedDropdown) {
            selectedDropdown.value = ''; // Reset the dropdown to its initial state
        }
    }

    // Reset all selection-related variables
    function resetSelections() {
        selectedTourId = null;
        selectedGuideId = null;
        selectedDropdown = null;
        confirmModal.hide();
    }
});


function removeIndividualTourGuide(buttonElement) {
    // Extract tourId and guideId from the button's data attributes
    const tourId = buttonElement.getAttribute('data-tour-id');
    const guideId = buttonElement.getAttribute('data-guide-id');
    const guideName = buttonElement.getAttribute('data-guide-name');

    if (!guideId) {
        showNotification('No guide is assigned to this tour.', 'error');
        return;
    }

    // Reference to the modal and its elements
    const confirmModal = new bootstrap.Modal(document.getElementById('confirmModal'));
    const confirmMessage = document.getElementById('confirmMessage');
    const confirmButton = document.getElementById('confirmButton');
    const cancelButton = document.querySelector('.btn-secondary[data-bs-dismiss="modal"]');
    const closeModalButton = document.querySelector('.btn-close[data-bs-dismiss="modal"]');

    // Set the confirmation message dynamically
    confirmMessage.textContent = `Are you sure you want to remove guide "${guideName}" from this individual tour?`;

    // Show the confirmation modal
    confirmModal.show();

    // Remove any existing click event listener from the confirm button to avoid duplication
    confirmButton.replaceWith(confirmButton.cloneNode(true));
    const newConfirmButton = document.getElementById('confirmButton');

    // Handle Yes button click
    newConfirmButton.addEventListener('click', function () {
        // Make the fetch request to remove the guide
        fetch(`/api/individual-tours/${tourId}/remove-guide`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ guideId }) // Sending guideId in the request body
        })
            .then((response) => {
                if (response.ok) {
                    // Show success notification
                    showNotification('Guide removed successfully!', 'success');

                    // Reload the page after a slight delay
                    setTimeout(() => location.reload(), 2000);
                } else {
                    return response.text().then((message) => {
                        throw new Error(message);
                    });
                }
            })
            .catch((error) => {
                console.error('Error removing guide:', error);

                // Show error notification
                showNotification(`Failed to remove guide. ${error.message}`, 'error');
            })
            .finally(() => {
                confirmModal.hide();
            });
    });

    // Handle No button click or modal close
    cancelButton.addEventListener('click', () => confirmModal.hide());
    closeModalButton.addEventListener('click', () => confirmModal.hide());
}


function cancelIndividualTour(buttonElement) {
    // Extract data from the button's data-* attributes
    const individualTourId = buttonElement.getAttribute("data-individual-tour-id");
    const studentName = buttonElement.getAttribute("data-student-name");
    const tourDate = buttonElement.getAttribute("data-tour-date");

    // Reference to the modal and its elements
    const confirmModal = new bootstrap.Modal(document.getElementById('confirmModal'));
    const confirmMessage = document.getElementById('confirmMessage');
    const confirmButton = document.getElementById('confirmButton');
    const cancelButton = document.querySelector('.btn-secondary[data-bs-dismiss="modal"]');
    const closeModalLabelButton = document.querySelector('.btn-close[data-bs-dismiss="modal"]');

    // Set the modal message dynamically
    confirmMessage.textContent = `Do you want to cancel the individual tour for ${studentName} on ${tourDate}? Note that the student will be notified and this action cannot be undone.`;

    // Remove any previous event listeners to avoid conflicts
    confirmButton.replaceWith(confirmButton.cloneNode(true));
    const newConfirmButton = document.getElementById('confirmButton');

    // Attach the specific cancellation logic to the "Yes" button
    newConfirmButton.addEventListener('click', function () {
        // Call the individual tour cancellation API
        fetch(`/api/individual-tours/${individualTourId}/cancel`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
        })
            .then((response) => {
                if (response.ok) {
                    // Show success notification
                    showNotification(`Individual tour for ${studentName} on ${tourDate} canceled successfully!`, "success");

                    // Optionally reload the page to reflect the changes
                    setTimeout(() => location.reload(), 2000);
                } else {
                    return response.text().then((message) => {
                        throw new Error(message);
                    });
                }
            })
            .catch((error) => {
                console.error("Error canceling individual tour:", error);

                // Show error notification
                showNotification(`Error: ${error.message}`, "error");
            })
            .finally(() => {
                confirmModal.hide();
            });
    });

    // Open the modal
    confirmModal.show();

    cancelButton.onclick = () => {
        confirmModal.hide();
    };

    closeModalLabelButton.onclick = () => {
        confirmModal.hide();
    };
}

// Note that to join an individual tour, already defined js functions are used because the logic is the same
document.addEventListener("DOMContentLoaded", () => {
    // Event delegation for Join Individual Tour button
    document.body.addEventListener("click", (event) => {
        if (event.target.classList.contains("join-individual-tour-button")) {
            const button = event.target;
            const tourId = button.getAttribute("data-tour-id");
            const guideId = button.getAttribute("data-guide-id");

            if (tourId && guideId) {
                assignIndividualTourGuide(tourId, guideId);
            }
        }
    });

    // Event delegation for Leave Individual Tour button
    document.body.addEventListener("click", (event) => {
        if (event.target.classList.contains("leave-individual-tour-button")) {
            const button = event.target;
            const tourId = button.getAttribute("data-tour-id");
            const guideId = button.getAttribute("data-guide-id");

            if (tourId && guideId) {
                removeIndividualTourGuide(button);
            }
        }
    });
});

function markIndividualTourAsCompleted(individualTourId) {
    console.log(`Marking individual tour as completed. Individual Tour ID: ${individualTourId}`);
    fetch(`/api/individual-tours/complete/${individualTourId}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
    })
        .then((response) => {
            if (response.ok) {
                alert(`Individual Tour ${individualTourId} marked as completed successfully!`);
                location.reload(); // Reload the page to reflect changes
            } else {
                return response.text().then((message) => {
                    throw new Error(message);
                });
            }
        })
        .catch((error) => {
            console.error("Error marking individual tour as completed:", error);
            alert(`Error: ${error.message}`);
        });
}

// Attach the click event listener for the "Mark As Completed" button for individual tours
document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll(".complete-individualTour-button").forEach((button) => {
        button.addEventListener("click", () => {
            const individualTourId = button.getAttribute("data-individualTour-id");
            markIndividualTourAsCompleted(individualTourId);
        });
    });
});

function showNotification(message, type = "success") {
    const notificationBox = document.getElementById("notification-box");

    // Set the message
    notificationBox.textContent = message;

    // Set the background color based on the type
    notificationBox.className = `notification ${type === "error" ? "error" : ""}`;

    // Show the notification
    notificationBox.style.display = "block";

    // Automatically hide the notification after 3 seconds
    setTimeout(() => {
        notificationBox.style.display = "none";
    }, 3000); // You can adjust the time as needed
}





