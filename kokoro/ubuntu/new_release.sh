#!/bin/bash
#
# Builds CT4E with Maven.
#

# Fail on any error.
set -e
# Display commands being run.
set -x

export CLOUDSDK_CORE_DISABLE_USAGE_REPORTING=true
#gcloud components update --quiet
#gcloud components install app-engine-java --quiet

echo "OAUTH_CLIENT_ID: ${OAUTH_CLIENT_ID}"
echo "OAUTH_CLIENT_SECRET: ${OAUTH_CLIENT_SECRET}"
echo "ANALYTICS_TRACKING_ID: ${ANALYTICS_TRACKING_ID}"
echo "PRODUCT_VERSION_SUFFIX: "${PRODUCT_VERSION_SUFFIX}

# Exit if undefined (zero-length).
test -n "${OAUTH_CLIENT_ID}"
test -n "${OAUTH_CLIENT_SECRET}"
test -n "${ANALYTICS_TRACKING_ID}"

cd git/google-cloud-eclipse

VERSION_SUFFIX_PROPERTY=
if [ "${PRODUCT_VERSION_SUFFIX}" ]; then
  VERSION_SUFFIX_PROPERTY="'${PRODUCT_VERSION_SUFFIX}'"
fi

# Need to unset `TMPDIR` for `xvfb-run` due to a bug:
# https://bugs.launchpad.net/ubuntu/+source/xorg-server/+bug/972324
TMPDIR= xvfb-run \
  mvn -V -B \
      -Dproduct.version.qualifier.suffix="${VERSION_SUFFIX_PROPERTY}" \
      -Doauth.client.id="${OAUTH_CLIENT_ID}" \
      -Doauth.client.secret="${OAUTH_CLIENT_SECRET}" \
      -Dga.tracking.id="${ANALYTICS_TRACKING_ID}" \
    clean package

# Also export `metadata.product` and `metadata.p2.inf` to the second Kokoro job.
readonly METADATA_DIR=gcp-repo/target/repository/metadata
mkdir "${METADATA_DIR}"
cp gcp-repo/metadata.p2.inf gcp-repo/metadata.product "${METADATA_DIR}"
