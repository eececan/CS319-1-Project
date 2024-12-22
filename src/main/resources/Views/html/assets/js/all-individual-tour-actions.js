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


function handleGuideSelection(selectElement) {
    const tourId = selectElement.getAttribute("data-tour-id");
    const guideId = selectElement.value;

    if (!guideId || !tourId) {
        alert("Invalid selection. Please try again.");
        return;
    }

    assignIndividualTourGuide(tourId, guideId);
}

function assignIndividualTourGuide(tourId, guideId) {
    if (!guideId) {
        alert('Please select a valid guide.');
        return;
    }

    fetch(`/api/individual-tours/${tourId}/assign-guide`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ guideId })
    })
        .then(response => {
            if (response.ok) {
                alert('Assignment is successful!');
                location.reload(); // Reload to reflect changes
            } else {
                return response.json().then((errorData) => {
                    throw new Error(errorData.error || 'Failed to assign guide.');
                });
            }
        })
        .catch(error => {
            console.error('Error assigning guide:', error);

            // Show a cleaner alert
            alert(error.message);

            // Reset the dropdown
            const dropdown = document.querySelector(`#individual-guide-${tourId}`);
            if (dropdown) {
                dropdown.selectedIndex = 0; // Reset to "Select Guide"
            }
        });
}

function removeIndividualTourGuide(buttonElement) {
    // Extract tourId and guideId from the button's data attributes
    const tourId = buttonElement.getAttribute('data-tour-id');
    const guideId = buttonElement.getAttribute('data-guide-id');

    if (!guideId) {
        alert('No guide is assigned to this tour.');
        return;
    }

    console.log("Tour ID:", tourId, "Guide ID:", guideId);

    // Make the fetch request to remove the guide
    fetch(`/api/individual-tours/${tourId}/remove-guide`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ guideId }) // Sending guideId in the request body
    })
        .then((response) => {
            if (response.ok) {
                alert('Guide removed successfully!');
                location.reload(); // Reload to reflect changes
            } else {
                return response.text().then((message) => {
                    throw new Error(message);
                });
            }
        })
        .catch((error) => {
            console.error('Error removing guide:', error);
            alert('Failed to remove guide. ' + error.message);
        });
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





