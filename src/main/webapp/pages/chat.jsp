<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Assistant – NetraaMart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .chat-box {
            max-width: 640px; margin: 0 auto;
            display: flex; flex-direction: column; height: 70vh;
            background: white; border: 1px solid var(--gray-200);
            border-radius: var(--radius); overflow: hidden;
        }
        .chat-header {
            padding: 1rem 1.25rem; background: var(--primary); color: white;
            font-weight: 600;
        }
        .chat-messages {
            flex: 1; overflow-y: auto; padding: 1rem;
            background: var(--gray-50);
        }
        .bubble {
            max-width: 85%; padding: 0.65rem 0.9rem; border-radius: 12px;
            margin-bottom: 0.65rem; line-height: 1.45; font-size: 0.95rem;
        }
        .bubble.user {
            background: var(--primary); color: white; margin-left: auto;
            border-bottom-right-radius: 4px;
        }
        .bubble.bot {
            background: white; border: 1px solid var(--gray-200);
            border-bottom-left-radius: 4px;
        }
        .chat-form {
            display: flex; gap: 0.5rem; padding: 0.75rem;
            border-top: 1px solid var(--gray-200); background: white;
        }
        .chat-form input {
            flex: 1; padding: 0.6rem 0.75rem; border: 1px solid var(--gray-200);
            border-radius: var(--radius); font-size: 1rem;
        }
        .hint { font-size: 0.85rem; color: var(--gray-600); margin: 0.75rem auto; max-width: 640px; }
    </style>
</head>
<body>
<nav class="navbar">
    <a href="${pageContext.request.contextPath}/" class="brand">NetraaMart</a>
    <nav>
        <a href="${pageContext.request.contextPath}/products">Browse</a>
        <a href="${pageContext.request.contextPath}/chat">Assistant</a>
        <c:if test="${not empty sessionScope.user}">
            <a href="${pageContext.request.contextPath}/cart">Cart</a>
            <a href="${pageContext.request.contextPath}/logout">Logout</a>
        </c:if>
        <c:if test="${empty sessionScope.user}">
            <a href="${pageContext.request.contextPath}/login">Login</a>
        </c:if>
    </nav>
</nav>

<div class="container">
    <p class="hint">
        Demo FAQ chatbot (no external API). Try: <em>How do I register?</em>, <em>checkout</em>, <em>orders</em>, <em>sell</em>
    </p>

    <div class="chat-box">
        <div class="chat-header">NetraaMart Assistant</div>
        <div class="chat-messages" id="messages">
            <div class="bubble bot">Hi! Ask me about shopping, selling, cart, checkout or orders.</div>
            <c:forEach var="m" items="${sessionScope.chatHistory}">
                <div class="bubble ${m.role == 'user' ? 'user' : 'bot'}">${m.text}</div>
            </c:forEach>
        </div>
        <form class="chat-form" method="post" action="${pageContext.request.contextPath}/chat">
            <input type="text" name="message" placeholder="Type your question..." required autocomplete="off">
            <button type="submit" class="btn btn-primary">Send</button>
        </form>
    </div>
</div>

<footer class="footer">NetraaMart &copy; 2026</footer>
<script>
  const box = document.getElementById('messages');
  if (box) box.scrollTop = box.scrollHeight;
</script>
</body>
</html>
