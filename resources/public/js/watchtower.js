

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
    });
  });
}

var interval = window.setInterval(reloadStatus, 2000)
