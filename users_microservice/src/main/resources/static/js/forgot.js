document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("forgotForm");
  const emailInput = document.getElementById("email");
  const emailError = document.getElementById("emailError");

  form.addEventListener("submit", async (event) => {
    event.preventDefault(); // prevent page reload

    // Reset error state
    emailError.textContent = "";
    emailInput.classList.remove("input-error");

    const email = emailInput.value.trim();

    if (!email) {
      emailError.textContent = "Email is required.";
      emailInput.classList.add("input-error"); // add red border class
      return;
    }

    try {
      const response = await fetch("/dev/api/auth/forgot-password", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({ emailAddress : email })
      });
console.log(JSON.stringify({ emailAddress : email }));
      if (!response.ok) {
        const errorData = await response.json();
        emailError.textContent = errorData.message || "Something went wrong.";
        emailInput.classList.add("input-error");
        return;
      }

      const data = await response.json();
      console.log(data);
      alert(data.message || "Password reset link sent to your email.");
      form.reset();
    } catch (err) {
        console.log(err);
      emailError.textContent = "Network error. Please try again.";
      emailInput.classList.add("input-error");
    }
  });
});