function handleIndividualTourApproval(individualTourId) {
    console.log("Approving individual tour with ID:", individualTourId);
    fetch(`/api/individual-tours/${individualTourId}/advisor/approve`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then((response) => {
            if (response.ok) {
                alert(`Individual Tour ID ${individualTourId} approved successfully!`);
                location.reload(); // Reload the page to reflect the updated status
            } else {
                return response.text().then((message) => {
                    throw new Error(message);
                });
            }
        })
        .catch((error) => {
            console.error("Error approving individual tour:", error);
            alert(`Error: ${error.message}`);
        });
}

function handleIndividualTourRejection(individualTourId) {
    console.log("Rejecting individual tour with ID:", individualTourId);
    fetch(`/api/individual-tours/${individualTourId}/advisor/reject`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then((response) => {
            if (response.ok) {
                alert(`Individual Tour ID ${individualTourId} rejected successfully!`);
                location.reload(); // Reload the page to reflect the updated status
            } else {
                return response.text().then((message) => {
                    throw new Error(message);
                });
            }
        })
        .catch((error) => {
            console.error("Error rejecting individual tour:", error);
            alert(`Error: ${error.message}`);
        });
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
                alert('Guide assigned successfully!');
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

function cancelIndividualTour(tourId) {
    fetch(`/api/individual-tours/${tourId}/cancel`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
    })
        .then((response) => {
            if (response.ok) {
                alert(`Individual Tour ID ${tourId} canceled successfully!`);
                location.reload(); // Reload the page to reflect the updated status
            } else {
                return response.text().then((message) => {
                    throw new Error(message);
                });
            }
        })
        .catch((error) => {
            console.error("Error canceling individual tour:", error);
            alert(`Error: ${error.message}`);
        });
}



