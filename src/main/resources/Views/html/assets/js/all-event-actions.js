function handleAdvisorApproval(buttonElement) {
    // Extract data from the button's data-* attributes
    const tourId = buttonElement.getAttribute("data-tour-id");
    const highSchoolName = buttonElement.getAttribute("data-highschool-name");
    const tourDate = buttonElement.getAttribute("data-tour-date");

    // Reference to the modal and its elements
    const confirmModal = new bootstrap.Modal(document.getElementById('confirmModal'));
    const confirmMessage = document.getElementById('confirmMessage');
    const confirmButton = document.getElementById('confirmButton');
    const cancelButton = document.querySelector('.btn-secondary[data-bs-dismiss="modal"]');
    const closeModalLabelButton = document.querySelector('.btn-close[data-bs-dismiss="modal"]');

    // Set the modal message dynamically
    confirmMessage.textContent = `Do you want to approve the tour for ${highSchoolName} on ${tourDate}? Note that this action cannot be undone.`;

    // Remove any previous event listeners to avoid conflicts
    confirmButton.replaceWith(confirmButton.cloneNode(true));
    const newConfirmButton = document.getElementById('confirmButton');

    // Attach the specific approval logic to the "Yes" button
    newConfirmButton.addEventListener('click', function () {
        // Call the approval API
        fetch(`/api/tours/${tourId}/advisor/approve`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
        })
            .then((response) => {
                if (response.ok) {
                    // Show success notification
                    showNotification(`Tour for ${highSchoolName} on ${tourDate} was approved successfully!`, "success");

                    // Optionally reload the page to reflect the changes
                    setTimeout(() => location.reload(), 2000);
                } else {
                    return response.text().then((message) => {
                        throw new Error(message);
                    });
                }
            })
            .catch((error) => {
                console.error("Error approving tour:", error);

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



/*function handleAdvisorApproval(tourId) {
    console.log("Approving tour with ID:", tourId);
    fetch(`/api/tours/${tourId}/advisor/approve`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then((response) => {
            if (response.ok) {
                alert(`Tour ID ${tourId} approved successfully!`);
                location.reload(); // Reload the page to reflect the updated status
            } else {
                return response.text().then((message) => {
                    throw new Error(message);
                });
            }
        })
        .catch((error) => {
            console.error("Error approving tour:", error);
            alert(`Error: ${error.message}`);
        });
}*/

function handleAdvisorRejection(buttonElement) {
    // Extract data from the button's data-* attributes
    const tourId = buttonElement.getAttribute("data-tour-id");
    const highSchoolName = buttonElement.getAttribute("data-highschool-name");
    const tourDate = buttonElement.getAttribute("data-tour-date");

    // Reference to the modal and its elements
    const confirmModal = new bootstrap.Modal(document.getElementById('confirmModal'));
    const confirmMessage = document.getElementById('confirmMessage');
    const confirmButton = document.getElementById('confirmButton');
    const cancelButton = document.querySelector('.btn-secondary[data-bs-dismiss="modal"]');
    const closeModalLabelButton = document.querySelector('.btn-close[data-bs-dismiss="modal"]');

    // Set the modal message dynamically
    confirmMessage.textContent = `Do you want to reject the tour application for ${highSchoolName} on ${tourDate}? Note that the high school will be notified and this action cannot be undone.`;

    // Remove any previous event listeners to avoid conflicts
    confirmButton.replaceWith(confirmButton.cloneNode(true));
    const newConfirmButton = document.getElementById('confirmButton');

    // Attach the specific rejection logic to the "Yes" button
    newConfirmButton.addEventListener('click', function () {
        // Call the rejection API
        fetch(`/api/tours/${tourId}/advisor/reject`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
        })
            .then((response) => {
                if (response.ok) {
                    // Show success notification
                    showNotification(`Tour for ${highSchoolName} on ${tourDate} was rejected successfully!`, "success");

                    // Optionally reload the page to reflect the changes
                    setTimeout(() => location.reload(), 2000);
                } else {
                    return response.text().then((message) => {
                        throw new Error(message);
                    });
                }
            })
            .catch((error) => {
                console.error("Error rejecting tour:", error);

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

function handleSecretaryApproval(buttonElement) {
    // Extract data from the button's data-* attributes
    const tourId = buttonElement.getAttribute("data-tour-id");
    const highSchoolName = buttonElement.getAttribute("data-highschool-name");
    const tourDate = buttonElement.getAttribute("data-tour-date");

    // Reference to the modal and its elements
    const confirmModal = new bootstrap.Modal(document.getElementById('confirmModal'));
    const confirmMessage = document.getElementById('confirmMessage');
    const confirmButton = document.getElementById('confirmButton');
    const cancelButton = document.querySelector('.btn-secondary[data-bs-dismiss="modal"]');
    const closeModalLabelButton = document.querySelector('.btn-close[data-bs-dismiss="modal"]');

    // Set the modal message dynamically
    confirmMessage.textContent = `Do you want to approve the tour for ${highSchoolName} on ${tourDate}? Note that the high school will be notified and this action cannot be undone.`;

    // Remove any previous event listeners to avoid conflicts
    confirmButton.replaceWith(confirmButton.cloneNode(true));
    const newConfirmButton = document.getElementById('confirmButton');

    // Attach the specific approval logic to the "Yes" button
    newConfirmButton.addEventListener('click', function () {
        // Call the approval API
        fetch(`/api/tours/${tourId}/secretary/approve`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
        })
            .then((response) => {
                if (response.ok) {
                    // Show success notification
                    showNotification(`Tour for ${highSchoolName} on ${tourDate} approved successfully!`, "success");

                    // Optionally reload the page to reflect the changes
                    setTimeout(() => location.reload(), 2000);
                } else {
                    return response.text().then((message) => {
                        throw new Error(message);
                    });
                }
            })
            .catch((error) => {
                console.error("Error approving tour:", error);

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

function handleSecretaryRejection(tourId) {
    fetch(`/api/tours/${tourId}/secretary/reject`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
    })
        .then((response) => {
            if (response.ok) {
                alert(`Tour ID ${tourId} rejected successfully!`);
                location.reload(); // Reload the page to reflect the updated status
            } else {
                return response.text().then((message) => {
                    throw new Error(message);
                });
            }
        })
        .catch((error) => {
            console.error("Error rejecting tour:", error);
            alert(`Error: ${error.message}`);
        });
}

function handleTourCancellation(buttonElement) {
    // Extract data from the button's data-* attributes
    const tourId = buttonElement.getAttribute("data-tour-id");
    const highSchoolName = buttonElement.getAttribute("data-highschool-name");
    const tourDate = buttonElement.getAttribute("data-tour-date");

    // Reference to the modal and its elements
    const confirmModal = new bootstrap.Modal(document.getElementById('confirmModal'));
    const confirmMessage = document.getElementById('confirmMessage');
    const confirmButton = document.getElementById('confirmButton');
    const cancelButton = document.querySelector('.btn-secondary[data-bs-dismiss="modal"]');
    const closeModalLabelButton = document.querySelector('.btn-close[data-bs-dismiss="modal"]');

    // Set the modal message dynamically
    confirmMessage.textContent = `Do you want to cancel the tour for ${highSchoolName} on ${tourDate}? Note that the high school will be notified and this action cannot be undone.`;

    // Remove any previous event listeners to avoid conflicts
    confirmButton.replaceWith(confirmButton.cloneNode(true));
    const newConfirmButton = document.getElementById('confirmButton');

    // Attach the specific cancellation logic to the "Yes" button
    newConfirmButton.addEventListener('click', function () {
        // Call the cancellation API
        fetch(`/api/tours/${tourId}/secretary/cancel`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
        })
            .then((response) => {
                if (response.ok) {
                    // Show success notification
                    showNotification(`Tour for ${highSchoolName} on ${tourDate} canceled successfully!`, "success");

                    // Optionally reload the page to reflect the changes
                    setTimeout(() => location.reload(), 2000);
                } else {
                    return response.text().then((message) => {
                        throw new Error(message);
                    });
                }
            })
            .catch((error) => {
                console.error("Error canceling tour:", error);

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
    const guideDropdowns = document.querySelectorAll('.guide-dropdown');
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
            selectedTourId = this.id.split('-')[1]; // Extract tourId from dropdown id
            selectedGuideId = this.value; // Get the selected guideId
            selectedDropdown = this; // Keep a reference to the specific dropdown

            const highSchoolName = this.dataset.highschoolname;
            const tourDate = this.dataset.tourdate;
            const tourHour = this.dataset.tourhour;
            const selectedOptionText = this.options[this.selectedIndex].text;

            confirmMessage.textContent = `You are about to assign guide "${selectedOptionText}" to the tour for ${highSchoolName} on ${tourDate}. Are you sure?`;
            confirmModal.show();
        });
    });

    // Handle Yes button click
    confirmButton.addEventListener('click', function () {
        if (selectedTourId && selectedGuideId) {
            fetch(`/api/tours/${selectedTourId}/assign-guide`, {
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
                        return response.text().then((message) => {
                            throw new Error(message);
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


// Thanks to this function, when the page is refreshed user returns to same table
document.addEventListener("DOMContentLoaded", function () {
    // Save the active tab to localStorage
    const tabs = document.querySelectorAll('.nav-tabs .nav-link');
    tabs.forEach(tab => {
        tab.addEventListener('click', function () {
            localStorage.setItem('activeTab', this.getAttribute('href'));
        });
    });

    // Restore the active tab from localStorage
    const activeTab = localStorage.getItem('activeTab');
    if (activeTab) {
        const tabToActivate = document.querySelector(`.nav-tabs .nav-link[href="${activeTab}"]`);
        if (tabToActivate) {
            tabToActivate.click();
        }
    }
});

document.addEventListener('DOMContentLoaded', function () {
    const addGuideSlotButtons = document.querySelectorAll('.add-guide-slot');

    addGuideSlotButtons.forEach((button) => {
        button.addEventListener('click', function () {
            const tourId = this.dataset.tourId;

            fetch(`/api/tours/${tourId}/increase-guide-count`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            })
                .then((response) => {
                    if (response.ok) {
                        // Show success notification
                        showNotification('Guide slot added successfully!', 'success');
                        setTimeout(() => location.reload(), 2000);
                    } else {
                        return response.text().then((message) => {
                            throw new Error(message);
                        });
                    }
                })
                .catch((error) => {
                    // Show error notification
                    showNotification(`Error: ${error.message}`, 'error');
                });
        });
    });
});

document.addEventListener("DOMContentLoaded", function () {
    // Select all "Remove Guide Slot" buttons
    const removeGuideSlotButtons = document.querySelectorAll(".remove-guide-slot");

    removeGuideSlotButtons.forEach((button) => {
        button.addEventListener("click", function () {
            const tourId = this.dataset.tourId; // Extract tourId from button's data attribute
            const guideIndex = this.dataset.guideIndex; // Extract the guide index

            // Send the request to the backend to decrease the guide count
            fetch(`/api/tours/${tourId}/decrease-guide-count`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ guideIndex: guideIndex }),
            })
                .then((response) => {
                    if (response.ok) {
                        // Show success notification
                        showNotification("Guide slot removed successfully!", "success");
                        setTimeout(() => location.reload(), 2000);
                    } else {
                        return response.text().then((message) => {
                            throw new Error(message);
                        });
                    }
                })
                .catch((error) => {
                    console.error("Error removing guide slot:", error);

                    // Show error notification
                    showNotification(`Error: ${error.message}`, "error");
                });
        });
    });
});

document.addEventListener('DOMContentLoaded', function () {
    const removeGuideButtons = document.querySelectorAll('.remove-guide');

    const confirmModal = new bootstrap.Modal(document.getElementById('confirmModal'));
    const confirmMessage = document.getElementById('confirmMessage');
    const confirmButton = document.getElementById('confirmButton');
    const cancelButton = document.querySelector('.btn-secondary[data-bs-dismiss="modal"]');
    const closeModalButton = document.querySelector('.btn-close[data-bs-dismiss="modal"]');

    let selectedTourId = null;
    let selectedGuideId = null;

    removeGuideButtons.forEach((button) => {
        button.addEventListener('click', function () {
            // Store the tour and guide IDs
            selectedTourId = this.dataset.tourId;
            selectedGuideId = this.dataset.guideId;

            // Set the confirmation message dynamically
            const guideName = this.dataset.guideName;
            const tourDate = this.dataset.tourDate;
            confirmMessage.textContent = `Are you sure you want to remove guide "${guideName}" from the tour on ${tourDate}?`;

            // Show the confirmation modal
            confirmModal.show();
        });
    });

    // Handle the confirmation action
    confirmButton.addEventListener('click', function () {
        if (selectedTourId && selectedGuideId) {
            // Send request to the server
            fetch(`/api/tours/${selectedTourId}/remove-guide`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ guideId: selectedGuideId }),
            })
                .then((response) => {
                    if (response.ok) {
                        // Show success notification
                        showNotification('Guide removed successfully!', 'success');
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
                    showNotification(`Error: ${error.message}`, 'error');
                })
                .finally(() => {
                    confirmModal.hide();
                });
        }
    });

    // Handle cancel action
    cancelButton.addEventListener('click', function () {
        confirmModal.hide();
    });

    // Handle close action
    closeModalButton.addEventListener('click', function () {
        confirmModal.hide();
    });
});


document.addEventListener("DOMContentLoaded", function () {
    const joinTourButtons = document.querySelectorAll(".join-tour-button");

    const confirmModal = new bootstrap.Modal(document.getElementById("confirmModal"));
    const confirmMessage = document.getElementById("confirmMessage");
    const confirmButton = document.getElementById("confirmButton");
    const cancelButton = document.querySelector(".btn-secondary[data-bs-dismiss='modal']");
    const closeModalButton = document.querySelector(".btn-close[data-bs-dismiss='modal']");

    let selectedTourId = null;
    let selectedGuideId = null;

    joinTourButtons.forEach((button) => {
        button.addEventListener("click", function () {
            // Get necessary data from button's data attributes
            selectedTourId = this.dataset.tourId;
            selectedGuideId = this.dataset.guideId;
            const schoolName = this.dataset.schoolName;
            const tourDate = this.dataset.tourDate;

            // Ensure guideId is available
            if (!selectedGuideId) {
                showNotification("Guide ID is missing.", "error");
                return;
            }

            // Set the confirmation message
            confirmMessage.innerHTML = `Are you sure you want to leave the tour for "${schoolName}" on ${tourDate}?<br><strong>Caution: You cannot leave if there are less than 7 days remaining!</strong>`;

            // Show the confirmation modal
            confirmModal.show();
        });
    });

    // Handle Yes button click
    confirmButton.addEventListener("click", function () {
        if (selectedTourId && selectedGuideId) {
            fetch(`/api/tours/${selectedTourId}/join`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ guideId: selectedGuideId }), // Pass guideId in the body
            })
                .then((response) => {
                    if (response.ok) {
                        // Show success notification
                        showNotification("Successfully joined the tour!", "success");

                        // Reload the page after a short delay
                        setTimeout(() => location.reload(), 2000);
                    } else {
                        return response.json().then((errorData) => {
                            throw new Error(errorData.error || "Failed to join the tour.");
                        });
                    }
                })
                .catch((error) => {
                    console.error("Error joining tour:", error);

                    // Show error notification
                    showNotification(`Error: ${error.message}`, "error");
                })
                .finally(() => {
                    confirmModal.hide();
                });
        }
    });

    // Handle No button click
    cancelButton.addEventListener("click", function () {
        confirmModal.hide();
        resetSelections();
    });

    // Handle Close (X) button click
    closeModalButton.addEventListener("click", function () {
        confirmModal.hide();
        resetSelections();
    });

    function resetSelections() {
        selectedTourId = null;
        selectedGuideId = null;
    }
});

document.addEventListener("DOMContentLoaded", function () {
    const leaveTourButtons = document.querySelectorAll(".leave-tour-button");

    const confirmModal = new bootstrap.Modal(document.getElementById("confirmModal"));
    const confirmMessage = document.getElementById("confirmMessage");
    const confirmButton = document.getElementById("confirmButton");
    const cancelButton = document.querySelector(".btn-secondary[data-bs-dismiss='modal']");
    const closeModalButton = document.querySelector(".btn-close[data-bs-dismiss='modal']");

    let selectedTourId = null;
    let selectedGuideId = null;

    leaveTourButtons.forEach((button) => {
        button.addEventListener("click", function () {
            // Get tour ID and guide ID from button's data attributes
            selectedTourId = this.dataset.tourId;
            selectedGuideId = this.dataset.guideId;
            const tourDate = this.dataset.tourDate;
            const schoolName = this.dataset.schoolName;

            // Ensure guideId is available
            if (!selectedGuideId) {
                showNotification("Guide ID is missing.", "error");
                return;
            }

            // Set the confirmation message
            confirmMessage.innerHTML = `Are you sure you want to leave the tour for "${schoolName}" on ${tourDate}?`;

            // Show the confirmation modal
            confirmModal.show();
        });
    });

    // Handle Yes button click
    confirmButton.addEventListener("click", function () {
        if (selectedTourId && selectedGuideId) {
            fetch(`/api/tours/${selectedTourId}/leave`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ guideId: selectedGuideId }), // Pass guideId in the body
            })
                .then((response) => {
                    if (response.ok) {
                        // Show success notification
                        showNotification("Successfully left the tour!", "success");

                        // Reload the page after a short delay
                        setTimeout(() => location.reload(), 2000);
                    } else {
                        return response.json().then((errorData) => {
                            throw new Error(errorData.error || "Failed to leave the tour.");
                        });
                    }
                })
                .catch((error) => {
                    console.error("Error leaving tour:", error);

                    // Show error notification
                    showNotification(`Error: ${error.message}`, "error");
                })
                .finally(() => {
                    confirmModal.hide();
                });
        }
    });

    // Handle No button click
    cancelButton.addEventListener("click", function () {
        confirmModal.hide();
        resetSelections();
    });

    // Handle Close (X) button click
    closeModalButton.addEventListener("click", function () {
        confirmModal.hide();
        resetSelections();
    });

    function resetSelections() {
        selectedTourId = null;
        selectedGuideId = null;
    }
});


document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.getElementById('schoolSearchInput');
    const searchButton = document.getElementById('searchButton');
    const clearButton = document.getElementById('clearButton');
    const daySelect = document.getElementById('eventDaySelect');

    // Clear button functionality
    if (clearButton) {
        clearButton.addEventListener('click', function() {
            searchInput.value = '';
            let url = new URL(window.location.href);
            url.searchParams.delete('search');

            // Preserve day filter if exists
            const dayFilter = daySelect?.value;
            if (dayFilter) {
                url.searchParams.set('dayFilter', dayFilter);
            }

            window.location.href = url.toString();
        });
    }

    // Combined search and day filter
    function updateFilters(searchTerm, dayFilter) {
        let url = new URL(window.location.href);
        let params = new URLSearchParams(url.search);

        // Update search parameter
        if (searchTerm) {
            params.set('search', searchTerm);
        } else {
            params.delete('search');
        }

        // Update day filter
        if (dayFilter) {
            params.set('dayFilter', dayFilter);
        } else {
            params.delete('dayFilter');
        }

        // Reset page numbers
        params.set('tourApplicationsPage', '0');
        params.set('toursPage', '0');
        params.set('fairsPage', '0');
        params.set('individualTourApplicationsPage', '0');
        params.set('individualToursPage', '0');

        url.search = params.toString();
        window.location.href = url.toString();
    }

    // Search functionality
    function performSearch() {
        const searchTerm = searchInput.value.trim();
        const dayFilter = daySelect?.value;
        updateFilters(searchTerm, dayFilter);
    }

    // Day filter change
    if (daySelect) {
        daySelect.addEventListener('change', function() {
            const searchTerm = searchInput.value.trim();
            const dayFilter = this.value;
            updateFilters(searchTerm, dayFilter);
        });
    }

    if (searchButton) {
        searchButton.addEventListener('click', performSearch);
    }

    if (searchInput) {
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                performSearch();
            }
        });
    }

    // Set initial values from URL
    const params = new URLSearchParams(window.location.search);
    if (searchInput) {
        searchInput.value = params.get('search') || '';
    }
    if (daySelect) {
        daySelect.value = params.get('dayFilter') || '';
    }
});

function markTourAsCompleted(tourId) {
    console.log(`Marking tour as completed. Tour ID: ${tourId}`);
    fetch(`/api/tours/complete/${tourId}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
    })
        .then((response) => {
            if (response.ok) {
                alert(`Tour ${tourId} marked as completed successfully!`);
                location.reload(); // Reload the page to reflect changes
            } else {
                return response.text().then((message) => {
                    throw new Error(message);
                });
            }
        })
        .catch((error) => {
            console.error("Error marking tour as completed:", error);
            alert(`Error: ${error.message}`);
        });
}

// Attach the click event listener for the "Mark As Completed" button
document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll(".complete-tour-button").forEach((button) => {
        button.addEventListener("click", () => {
            const tourId = button.getAttribute("data-tour-id");
            markTourAsCompleted(tourId);
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

