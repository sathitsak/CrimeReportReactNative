
/**
 * University of Melbourne
 * School and Computing and Information Systems
 * SWEN90014 Masters Software Engineering Project
 * Team Bilby
 */

// Used as a pre sign up trigger lambda to auto confirm created user

exports.handler = (event, context, callback) => {
    event.response.autoConfirmUser = true;
    context.done(null, event);
};