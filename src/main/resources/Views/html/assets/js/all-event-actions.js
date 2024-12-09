function handleAdvisorApproval(tourId) {
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
}

function handleAdvisorRejection(tourId) {
    fetch(`/api/tours/${tourId}/advisor/reject`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        }
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

function handleSecretaryApproval(tourId) {
    fetch(`/api/tours/${tourId}/secretary/approve`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
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

function handleTourCancellation(tourId) {
    fetch(`/api/tours/${tourId}/secretary/cancel`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
    })
        .then((response) => {
            if (response.ok) {
                alert(`Tour ID ${tourId} canceled successfully!`);
                location.reload(); // Reload the page to reflect the updated status
            } else {
                return response.text().then((message) => {
                    throw new Error(message);
                });
            }
        })
        .catch((error) => {
            console.error("Error canceling tour:", error);
            alert(`Error: ${error.message}`);
        });
}

/*document.addEventListener('DOMContentLoaded', function () {
    const guideDropdowns = document.querySelectorAll('.guide-dropdown');

    guideDropdowns.forEach((dropdown) => {
        dropdown.addEventListener('change', function () {
            const tourId = this.id.split('-')[1]; // Extract tourId from dropdown id
            const guideId = this.value; // Get the selected guideId

            // Send the data to the server
            fetch(`/api/tours/${tourId}/assign-guide`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ guideId }),
            })
                .then((response) => {
                    if (response.ok) {
                        alert('Guide assigned successfully!');
                        location.reload(); // Reload the page to reflect changes
                    } else {
                        return response.text().then((message) => {
                            throw new Error(message);
                        });
                    }
                })
                .catch((error) => {
                    console.error('Error assigning guide:', error);
                    alert(`Error: ${error.message}`);
                });
        });
    });
});*/

document.addEventListener('DOMContentLoaded', function () {
    let selectedTourId = null;
    let selectedGuideId = null;
    const guideDropdowns = document.querySelectorAll('.guide-dropdown');
    const confirmModal = new bootstrap.Modal(document.getElementById('confirmModal'));
    const confirmButton = document.getElementById('confirmButton');
    const cancelButton = document.getElementById('cancelButton');

    guideDropdowns.forEach((dropdown) => {
        dropdown.addEventListener('change', function () {
            // Extract data from the dropdown
            const tourId = this.id.split('-')[1];
            const guideId = this.value;
            const guideName = this.options[this.selectedIndex].text;
            const highSchoolName = this.dataset.highschoolname;
            const tourDate = this.dataset.tourdate;
            const tourHour = this.dataset.tourhour;

            // Set up confirmation message
            const modalMessage = `You are about to assign guide "${guideName}" to the tour for ${highSchoolName} on ${tourDate} at ${tourHour}. Are you sure?`;
            document.getElementById('confirmMessage').textContent = modalMessage;

            // Store selection data
            selectedTourId = tourId;
            selectedGuideId = guideId;

            // Show the confirmation modal
            confirmModal.show();
        });
    });

    // Handle Yes button click
    confirmButton.addEventListener('click', function () {
        if (selectedTourId && selectedGuideId) {
            // Perform the assignment
            fetch(`/api/tours/${selectedTourId}/assign-guide`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ guideId: selectedGuideId }),
            })
                .then((response) => {
                    if (response.ok) {
                        alert('Guide assigned successfully!');
                        location.reload(); // Reload to reflect changes
                    } else {
                        return response.text().then((message) => {
                            throw new Error(message);
                        });
                    }
                })
                .catch((error) => {
                    console.error('Error assigning guide:', error);
                    alert(`Error: ${error.message}`);
                });

            // Hide the modal
            confirmModal.hide();
        }
    });

    // Handle No button click
    cancelButton.addEventListener('click', function () {
        // Simply hide the modal and reset the dropdown selection
        const dropdown = document.querySelector(`#guide-${selectedTourId}-1`);
        if (dropdown) {
            dropdown.value = ''; // Reset the dropdown to its initial state
        }

        selectedTourId = null;
        selectedGuideId = null;
        // Hide the modal
        confirmModal.hide();
    });

    // Handle No button click
    cancelButton.addEventListener('click', function () {
        // Simply hide the modal and reset the dropdown selection
        const dropdown = document.querySelector(`#guide-${selectedTourId}-1`);
        if (dropdown) {
            dropdown.value = ''; // Reset the dropdown to its initial state
        }

        selectedTourId = null;
        selectedGuideId = null;
        // Hide the modal
        confirmModal.hide();
    });

    closeModalLabelButton.addEventListener('click', function () {
        // Simply hide the modal and reset the dropdown selection
        confirmModal.hide();
    });
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


