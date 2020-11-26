BRANCH="master"

# Are we on the right branch?
if [ "$TRAVIS_BRANCH" = "$BRANCH" ]; then
  
  # Is this not a Pull Request?
  if [ "$TRAVIS_PULL_REQUEST" = false ]; then
    
    # Is this not a build which was triggered by setting a new tag?
    if [ -z "$TRAVIS_TAG" ]; then
      echo -e "Starting to tag commit.\n"

      git config --global user.email "travis@travis-ci.org"
      git config --global user.name "Travis"

      # increment version code
      ./gradlew bumperVersionPatch

      # read version name
      FILE_NAME=app/gradle.properties
      KEY="appVersionName"
      value=""

      getProperty()
      {
        prop_key=$1
        value=`cat ${FILE_NAME} | grep ${prop_key} | cut -d'=' -f2`
      }
      getProperty ${KEY}

      # Add tag and push to master.
      git tag -a v${value} -m "Travis build $value pushed a tag."
      git push origin --tags
      git fetch origin

      echo -e "Done magic with tags.\n"
  fi
  fi
fi