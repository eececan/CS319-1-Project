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
            selectedFairId = this.id.split('-')[1];
            selectedGuideId = this.value;
            selectedDropdown = this;

            const highSchoolName = this.dataset.highschoolname;
            const fairDate = this.dataset.fairdate;
            const fairHour = this.dataset.fairhour;
            const selectedOptionText = this.options[this.selectedIndex].text;

            confirmMessage.textContent = `You are about to assign guide "${selectedOptionText}" to the fair for ${highSchoolName} on ${fairDate}. Are you sure?`;
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
                        alert('Guide assigned successfully!');
                        location.reload();
                    } else {
                        return response.text().then((message) => {
                            throw new Error(message);
                        });
                    }
                })
                .catch((error) => {
                    console.error('Error assigning guide:', error);
                    alert(`Error: ${error.message}`);
                    resetDropdown();
                })
                .finally(() => {
                    resetSelections();
                });

            confirmModal.hide();
        }
    });

    cancelButton.addEventListener('click', function () {
        resetDropdown();
        resetSelections();
    });

    closeModalLabelButton.addEventListener('click', function () {
        resetDropdown();
        resetSelections();
    });

    function resetDropdown() {
        if (selectedDropdown) {
            selectedDropdown.value = '';
        }
    }

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
                        alert('Guide slot added successfully!');
                        location.reload();
                    } else {
                        return response.text().then((message) => {
                            throw new Error(message);
                        });
                    }
                })
                .catch((error) => {
                    alert(`Error: ${error.message}`);
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

            if (confirm("Are you sure you want to remove this guide slot?")) {
                fetch(`/api/fairs/${fairId}/decrease-guide-count`, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({ guideIndex: guideIndex }),
                })
                    .then((response) => {
                        if (response.ok) {
                            alert("Guide slot removed successfully!");
                            location.reload();
                        } else {
                            return response.text().then((message) => {
                                throw new Error(message);
                            });
                        }
                    })
                    .catch((error) => {
                        console.error("Error removing guide slot:", error);
                        alert(`Error: ${error.message}`);
                    });
            }
        });
    });
});

document.addEventListener('DOMContentLoaded', function () {
    const removeGuideButtons = document.querySelectorAll('.remove-fair-guide');

    removeGuideButtons.forEach((button) => {
        button.addEventListener('click', function () {
            const fairId = this.dataset.fairId;
            const guideId = this.dataset.guideId;

            if (!confirm("Are you sure you want to remove this guide from the fair?")) {
                return;
            }

            fetch(`/api/fairs/${fairId}/remove-guide`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ guideId: guideId }),
            })
                .then((response) => {
                    if (response.ok) {
                        alert('Guide removed successfully!');
                        location.reload();
                    } else {
                        return response.text().then((message) => {
                            throw new Error(message);
                        });
                    }
                })
                .catch((error) => {
                    console.error('Error removing guide:', error);
                    alert(`Error: ${error.message}`);
                });
        });
    });
});

document.addEventListener("DOMContentLoaded", function () {
    const joinFairButtons = document.querySelectorAll(".join-fair-button");

    joinFairButtons.forEach((button) => {
        button.addEventListener("click", function () {
            const fairId = this.dataset.fairId;
            const guideId = this.dataset.guideId;

            if (!guideId) {
                alert("Guide ID is missing.");
                return;
            }

            fetch(`/api/fairs/${fairId}/join`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ guideId: guideId }),
            })
                .then((response) => {
                    if (response.ok) {
                        alert("Successfully joined the fair!");
                        location.reload();
                    } else {
                        return response.text().then((message) => {
                            throw new Error(message);
                        });
                    }
                })
                .catch((error) => {
                    console.error("Error joining fair:", error);
                    alert(`Error: ${error.message}`);
                });
        });
    });
});

document.addEventListener("DOMContentLoaded", function () {
    const leaveFairButtons = document.querySelectorAll(".leave-fair-button");

    leaveFairButtons.forEach((button) => {
        button.addEventListener("click", function () {
            const fairId = this.dataset.fairId;
            const guideId = this.dataset.guideId;

            if (!guideId) {
                alert("Guide ID is missing.");
                return;
            }

            fetch(`/api/fairs/${fairId}/leave`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ guideId: guideId }),
            })
                .then((response) => {
                    if (response.ok) {
                        alert("Successfully left the fair!");
                        location.reload();
                    } else {
                        return response.text().then((message) => {
                            throw new Error(message);
                        });
                    }
                })
                .catch((error) => {
                    console.error("Error leaving fair:", error);
                    alert(`Error: ${error.message}`);
                });
        });
    });
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
