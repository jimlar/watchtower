
// Pulsate opacity
function startPulsating(object) {
  if (object.data('pulsate')) {
    return;
  }
  object.data('pulsate', true);
  function pulsate() {
    object.fadeOut(500, function() {
      object.fadeIn(500, function() {
        if(object.data('pulsate')) {
          window.setTimeout(pulsate, 1);
        } else {
          object.data('pulsate', null);
        }
      })
    });
  }
  window.setTimeout(pulsate, 1);
}

// Stop pulsating
function stopPulsating(object) {
  object.data('pulsate', false);
}

// Reload status from Jenkins
function reloadStatus() {
  url = document.location.pathname;
  if (url.charAt(url.length - 1) != '/') {
    url = url + '/';
  }
  $.getJSON(url + 'jobs.json', function(data) {
    $.each(data, function(i, job) {
      var id = job['id'];
      job_row = $('#' + id);
      status_button = job_row.find('span.status').first();
      culprits = job_row.find('.culprits');

      culprits.html($.map(job['culprits'], function(culprit) {
        return '<span class="label label-info">' + culprit + '</span> ';
      }));

      if (job['successful']) {
        status_button.addClass('btn-success').removeClass('btn-danger').text('OK');
        culprits.hide();
      } else {
        status_button.removeClass('btn-success').addClass('btn-danger').text('FAIL');
        culprits.show();
      }

      if (job['building']) {
        startPulsating(status_button);
      } else {
        stopPulsating(status_button);
      }
    });
  });
}

// Keep reloading 
var interval = window.setInterval(reloadStatus, 2000)
