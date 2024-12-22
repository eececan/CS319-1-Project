function handleFairApproval(buttonElement) {
    // Extract data from the button's data-* attributes
    const fairId = buttonElement.getAttribute("data-fair-id");
    const schoolName = buttonElement.getAttribute("data-school-name");
    const fairDate = buttonElement.getAttribute("data-fair-date");

    // Reference to the modal and its elements
    const confirmModal = new bootstrap.Modal(document.getElementById('confirmModal'));
    const confirmMessage = document.getElementById('confirmMessage');
    const confirmButton = document.getElementById('confirmButton');
    const cancelButton = document.querySelector('.btn-secondary[data-bs-dismiss="modal"]');
    const closeModalLabelButton = document.querySelector('.btn-close[data-bs-dismiss="modal"]');

    // Set the modal message dynamically
    confirmMessage.textContent = `Do you want to approve the fair for ${schoolName} on ${fairDate}? Note that the high school will be notified and this action cannot be undone.`;

    // Remove any previous event listeners to avoid conflicts
    confirmButton.replaceWith(confirmButton.cloneNode(true));
    const newConfirmButton = document.getElementById('confirmButton');

    // Attach the specific approval logic to the "Yes" button
    newConfirmButton.addEventListener('click', function () {
        // Call the fair approval API
        fetch(`/api/fairs/approve/${fairId}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
        })
            .then((response) => {
                if (response.ok) {
                    // Show success notification
                    showNotification(`Fair for ${schoolName} on ${fairDate} approved successfully!`, "success");

                    // Optionally reload the page to reflect the changes
                    setTimeout(() => location.reload(), 2000);
                } else {
                    return response.text().then((message) => {
                        throw new Error(message);
                    });
                }
            })
            .catch((error) => {
                console.error("Error approving fair:", error);

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

function handleFairRejection(buttonElement) {
    // Extract data from the button's data-* attributes
    const fairId = buttonElement.getAttribute("data-fair-id");
    const schoolName = buttonElement.getAttribute("data-school-name");
    const fairDate = buttonElement.getAttribute("data-fair-date");

    // Reference to the modal and its elements
    const confirmModal = new bootstrap.Modal(document.getElementById('confirmModal'));
    const confirmMessage = document.getElementById('confirmMessage');
    const confirmButton = document.getElementById('confirmButton');
    const cancelButton = document.querySelector('.btn-secondary[data-bs-dismiss="modal"]');
    const closeModalLabelButton = document.querySelector('.btn-close[data-bs-dismiss="modal"]');

    // Set the modal message dynamically
    confirmMessage.textContent = `Do you want to reject the fair for ${schoolName} on ${fairDate}? Note that the high school will be notified and this action cannot be undone.`;

    // Remove any previous event listeners to avoid conflicts
    confirmButton.replaceWith(confirmButton.cloneNode(true));
    const newConfirmButton = document.getElementById('confirmButton');

    // Attach the specific rejection logic to the "Yes" button
    newConfirmButton.addEventListener('click', function () {
        // Call the fair rejection API
        fetch(`/api/fairs/reject/${fairId}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
        })
            .then((response) => {
                if (response.ok) {
                    // Show success notification
                    showNotification(`Fair for ${schoolName} on ${fairDate} rejected successfully!`, "success");

                    // Optionally reload the page to reflect the changes
                    setTimeout(() => location.reload(), 2000);
                } else {
                    return response.text().then((message) => {
                        throw new Error(message);
                    });
                }
            })
            .catch((error) => {
                console.error("Error rejecting fair:", error);

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


function handleFairCancellation(buttonElement) {
    // Extract data from the button's data-* attributes
    const fairId = buttonElement.getAttribute("data-fair-id");
    const schoolName = buttonElement.getAttribute("data-school-name");
    const fairDate = buttonElement.getAttribute("data-fair-date");

    // Reference to the modal and its elements
    const confirmModal = new bootstrap.Modal(document.getElementById('confirmModal'));
    const confirmMessage = document.getElementById('confirmMessage');
    const confirmButton = document.getElementById('confirmButton');
    const cancelButton = document.querySelector('.btn-secondary[data-bs-dismiss="modal"]');
    const closeModalLabelButton = document.querySelector('.btn-close[data-bs-dismiss="modal"]');

    // Set the modal message dynamically
    confirmMessage.textContent = `Do you want to cancel the fair for ${schoolName} on ${fairDate}? Note that the high school will be notified and the guides assigned to this tour will be removed. This action cannot be undone.`;

    // Remove any previous event listeners to avoid conflicts
    confirmButton.replaceWith(confirmButton.cloneNode(true));
    const newConfirmButton = document.getElementById('confirmButton');

    // Attach the specific cancellation logic to the "Yes" button
    newConfirmButton.addEventListener('click', function () {
        // Call the fair cancellation API
        fetch(`/api/fairs/${fairId}/cancel`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
        })
            .then((response) => {
                if (response.ok) {
                    // Show success notification
                    showNotification(`Fair for ${schoolName} on ${fairDate} canceled successfully!`, "success");

                    // Optionally reload the page to reflect the changes
                    setTimeout(() => location.reload(), 2000);
                } else {
                    return response.text().then((message) => {
                        throw new Error(message);
                    });
                }
            })
            .catch((error) => {
                console.error("Error canceling fair:", error);

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
    const guideDropdowns = document.querySelectorAll('.guide-fair-dropdown');
    let selectedFairId = null;
    let selectedGuideId = null;
    let selectedDropdown = null;

    const confirmModal = new bootstrap.Modal(document.getElementById('confirmModal'));
    const confirmMessage = document.getElementById('confirmMessage');
    const confirmButton = document.getElementById('confirmButton');
    const cancelButton = document.querySelector('.btn-secondary[data-bs-dismiss="modal"]');
    const closeModalLabelButton = document.querySelector('.btn-close[data-bs-dismiss="modal"]');

    guideDropdowns.forEach((dropdown) => {
        dropdown.addEventListener('change', function () {
            selectedFairId = this.id.split('-')[1]; // Extract fair ID from dropdown ID
            selectedGuideId = this.value; // Get the selected guide ID
            selectedDropdown = this;

            const highSchoolName = this.dataset.highschoolname;
            const fairDate = this.dataset.fairdate;
            const fairHour = this.dataset.fairhour;
            const selectedOptionText = this.options[this.selectedIndex].text;

            // Set the confirmation message
            confirmMessage.innerHTML = `You are about to assign guide <strong>"${selectedOptionText}"</strong> to the fair for <strong>${highSchoolName}</strong> on <strong>${fairDate} at ${fairHour}</strong>. Are you sure?`;

            // Show confirmation modal
            confirmModal.show();
        });
    });

    confirmButton.addEventListener('click', function () {
        if (selectedFairId && selectedGuideId) {
            fetch(`/api/fairs/${selectedFairId}/assign-guide`, {
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

                        // Reload the page after a short delay
                        setTimeout(() => location.reload(), 2000);
                    } else {
                        // Attempt to parse the error as JSON, fallback to plain text
                        return response.text().then((message) => {
                            try {
                                const errorData = JSON.parse(message);
                                throw new Error(errorData.error || 'An unknown error occurred.');
                            } catch (err) {
                                throw new Error(message || 'An unknown error occurred.');
                            }
                        });
                    }
                })
                .catch((error) => {
                    console.error('Error assigning guide:', error);

                    // Show backend error notification
                    showNotification(`Error: ${error.message}`, 'error');
                    // Reload the page after a short delay
                    setTimeout(() => location.reload(), 2000);
                })
                .finally(() => {
                    confirmModal.hide();
                    resetSelections();
                });
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
            selectedDropdown.value = ''; // Reset dropdown to default state
        }
    }

    // Reset all selection-related variables
    function resetSelections() {
        selectedFairId = null;
        selectedGuideId = null;
        selectedDropdown = null;
        confirmModal.hide();
    }
});


document.addEventListener('DOMContentLoaded', function () {
    const addGuideSlotButtons = document.querySelectorAll('.add-fair-guide-slot');

    addGuideSlotButtons.forEach((button) => {
        button.addEventListener('click', function () {
            const fairId = this.dataset.fairId;

            fetch(`/api/fairs/${fairId}/increase-guide-count`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            })
                .then((response) => {
                    if (response.ok) {
                        // Show success notification
                        showNotification('Guide slot added successfully!', 'success');

                        // Reload the page after a short delay
                        setTimeout(() => location.reload(), 2000);
                    } else {
                        return response.text().then((message) => {
                            throw new Error(message);
                        });
                    }
                })
                .catch((error) => {
                    console.error('Error adding guide slot:', error);

                    // Show error notification
                    showNotification(`Error: ${error.message}`, 'error');
                });
        });
    });
});


document.addEventListener("DOMContentLoaded", function () {
    const removeGuideSlotButtons = document.querySelectorAll(".remove-fair-guide-slot");

    removeGuideSlotButtons.forEach((button) => {
        button.addEventListener("click", function () {
            const fairId = this.dataset.fairId;
            const guideIndex = this.dataset.guideIndex;

            fetch(`/api/fairs/${fairId}/decrease-guide-count`, {
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

                        // Reload the page after a short delay
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
    const removeGuideButtons = document.querySelectorAll('.remove-fair-guide');

    const confirmModal = new bootstrap.Modal(document.getElementById('confirmModal'));
    const confirmMessage = document.getElementById('confirmMessage');
    const confirmButton = document.getElementById('confirmButton');
    const cancelButton = document.querySelector('.btn-secondary[data-bs-dismiss="modal"]');
    const closeModalButton = document.querySelector('.btn-close[data-bs-dismiss="modal"]');

    let selectedFairId = null;
    let selectedGuideId = null;

    removeGuideButtons.forEach((button) => {
        button.addEventListener('click', function () {
            selectedFairId = this.dataset.fairId;
            selectedGuideId = this.dataset.guideId;
            const guideName = this.dataset.guideName;
            const fairName = this.dataset.fairName;
            const fairDate = this.dataset.fairDate;
            const fairHour = this.dataset.fairHour;

            if (!selectedFairId || !selectedGuideId) {
                showNotification("Fair ID or Guide ID is missing.", "error");
                return;
            }

            // Set the confirmation message
            confirmMessage.innerHTML = `You are about to remove guide <strong>"${guideName}"</strong> from the fair <strong>${fairName}</strong> on <strong>${fairDate} at ${fairHour}</strong>. Are you sure?`;

            // Show the confirmation modal
            confirmModal.show();
        });
    });

    // Handle Yes button click
    confirmButton.addEventListener('click', function () {
        if (selectedFairId && selectedGuideId) {
            fetch(`/api/fairs/${selectedFairId}/remove-guide`, {
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

                        // Reload the page after a short delay
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
                    resetSelections();
                });
        }
    });

    // Handle No button click
    cancelButton.addEventListener('click', function () {
        confirmModal.hide();
        resetSelections();
    });

    // Handle Close (X) button click
    closeModalButton.addEventListener('click', function () {
        confirmModal.hide();
        resetSelections();
    });

    // Reset selected IDs
    function resetSelections() {
        selectedFairId = null;
        selectedGuideId = null;
    }
});

document.addEventListener("DOMContentLoaded", function () {
    const joinFairButtons = document.querySelectorAll(".join-fair-button");

    const confirmModal = new bootstrap.Modal(document.getElementById("confirmModal"));
    const confirmMessage = document.getElementById("confirmMessage");
    const confirmButton = document.getElementById("confirmButton");
    const cancelButton = document.querySelector(".btn-secondary[data-bs-dismiss='modal']");
    const closeModalButton = document.querySelector(".btn-close[data-bs-dismiss='modal']");

    let selectedFairId = null;
    let selectedGuideId = null;

    joinFairButtons.forEach((button) => {
        button.addEventListener("click", function () {
            // Get fair ID and guide ID from button's data attributes
            selectedFairId = this.dataset.fairId;
            selectedGuideId = this.dataset.guideId;
            const fairName = this.dataset.fairName;
            const fairDate = this.dataset.fairDate;

            if (!selectedGuideId) {
                showNotification("Guide ID is missing.", "error");
                return;
            }

            // Set the confirmation message
            confirmMessage.innerHTML = `Are you sure you want to join the fair "${fairName}" on ${fairDate}? <br><strong>Caution: You cannot leave if there are less than 7 days remaining!</strong>`;

            // Show the confirmation modal
            confirmModal.show();
        });
    });

    // Handle Yes button click
    confirmButton.addEventListener("click", function () {
        if (selectedFairId && selectedGuideId) {
            fetch(`/api/fairs/${selectedFairId}/join`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ guideId: selectedGuideId }),
            })
                .then((response) => {
                    if (response.ok) {
                        // Show success notification
                        showNotification("Successfully joined the fair!", "success");

                        // Reload the page after a short delay
                        setTimeout(() => location.reload(), 2000);
                    } else {
                        return response.json().then((errorData) => {
                            throw new Error(errorData.error || "Failed to join the fair.");
                        });
                    }
                })
                .catch((error) => {
                    console.error("Error joining fair:", error);

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
        selectedFairId = null;
        selectedGuideId = null;
    }
});

document.addEventListener("DOMContentLoaded", function () {
    const leaveFairButtons = document.querySelectorAll(".leave-fair-button");

    const confirmModal = new bootstrap.Modal(document.getElementById("confirmModal"));
    const confirmMessage = document.getElementById("confirmMessage");
    const confirmButton = document.getElementById("confirmButton");
    const cancelButton = document.querySelector(".btn-secondary[data-bs-dismiss='modal']");
    const closeModalButton = document.querySelector(".btn-close[data-bs-dismiss='modal']");

    let selectedFairId = null;
    let selectedGuideId = null;

    leaveFairButtons.forEach((button) => {
        button.addEventListener("click", function () {
            // Get fair ID and guide ID from button's data attributes
            selectedFairId = this.dataset.fairId;
            selectedGuideId = this.dataset.guideId;
            const fairName = this.dataset.fairName;
            const fairDate = this.dataset.fairDate;

            if (!selectedGuideId) {
                showNotification("Guide ID is missing.", "error");
                return;
            }

            // Set the confirmation message
            confirmMessage.innerHTML = `Are you sure you want to leave the fair "${fairName}" scheduled on ${fairDate}?`;

            // Show the confirmation modal
            confirmModal.show();
        });
    });

    // Handle Yes button click
    confirmButton.addEventListener("click", function () {
        if (selectedFairId && selectedGuideId) {
            fetch(`/api/fairs/${selectedFairId}/leave`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ guideId: selectedGuideId }),
            })
                .then((response) => {
                    if (response.ok) {
                        // Show success notification
                        showNotification("Successfully left the fair!", "success");

                        // Reload the page after a short delay
                        setTimeout(() => location.reload(), 2000);
                    } else {
                        return response.json().then((errorData) => {
                            throw new Error(errorData.error || "Failed to leave the fair.");
                        });
                    }
                })
                .catch((error) => {
                    console.error("Error leaving fair:", error);

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
        selectedFairId = null;
        selectedGuideId = null;
    }
});


function markFairAsCompleted(fairId) {
    console.log(`Marking fair as completed. Fair ID: ${fairId}`);
    fetch(`/api/fairs/complete/${fairId}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
    })
        .then((response) => {
            if (response.ok) {
                alert(`Fair ${fairId} marked as completed successfully!`);
                location.reload(); // Reload the page to reflect changes
            } else {
                return response.text().then((message) => {
                    throw new Error(message);
                });
            }
        })
        .catch((error) => {
            console.error("Error marking fair as completed:", error);
            alert(`Error: ${error.message}`);
        });
}

// Attach the click event listener for the "Mark As Completed" button
document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll(".complete-fair-button").forEach((button) => {
        button.addEventListener("click", () => {
            const fairId = button.getAttribute("data-fair-id");
            markFairAsCompleted(fairId);
        });
    });
});
